package im.nll.data.spider.crawler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A Map that converts all keys to lowercase Strings for case insensitive
 * lookups.  This is needed for the toMap() implementation because
 * databases don't consistently handle the casing of column names.
 * <p>
 * <p>The keys are stored as they are given [BUG #DBUTILS-34], so we maintain
 * an internal mapping from lowercase keys to the real keys in order to
 * achieve the case insensitive lookup.
 * <p>
 * <p>Note: This implementation does not allow {@code null}
 * for key, whereas {@link LinkedHashMap} does, because of the code:
 * <pre>
 * key.toString().toLowerCase()
 * </pre>
 */
public class CaseInsensitiveHashMap extends LinkedHashMap<String, String> {
    /**
     * The internal mapping from lowercase keys to the real keys.
     * <p/>
     * <p>
     * Any query operation using the key
     * ({@link #get(Object)}, {@link #containsKey(Object)})
     * is done in three steps:
     * <ul>
     * <li>convert the parameter key to lower case</li>
     * <li>get the actual key that corresponds to the lower case key</li>
     * <li>query the map with the actual key</li>
     * </ul>
     * </p>
     */
    private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = -2848100435296897392L;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.containsKey(realKey);
        // Possible optimisation here:
        // Since the lowerCaseMap contains a mapping for all the keys,
        // we could just do this:
        // return lowerCaseMap.containsKey(key.toString().toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.get(realKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String put(String key, String value) {
            /*
             * In order to keep the map and lowerCaseMap synchronized,
             * we have to remove the old mapping before putting the
             * new one. Indeed, oldKey and key are not necessaliry equals.
             * (That's why we call super.remove(oldKey) and not just
             * super.put(key, value))
             */
        Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        Object oldValue = super.remove(oldKey);
        super.put(key, value);
        return (String) oldValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            this.put(key, value);
        }
    }

    @Override
    public String remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return super.remove(realKey);
    }

}