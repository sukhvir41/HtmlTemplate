package org.ht.template;

class ParameterKey {

    private String key;
    private Class<?> theClass;

    public ParameterKey(String key, Class<?> theClass) {
        this.key = key;
        this.theClass = theClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterKey that = (ParameterKey) o;

        if (!key.equals(that.key)) return false;
        return theClass.equals(that.theClass);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + theClass.hashCode();
        return result;
    }
}
