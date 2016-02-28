package nl.tue.s2id90.group20.transposition;

/**
 * A pair of keys used for state identification. 
 */
public class Keypair {
    //first key.
    public final long key1;
    
    //second key.
    public final long key2;

    /**
     * Create keypair from two integers.
     * 
     * @param key1: first key.
     * @param key2: second key.
     */
    public Keypair(long key1, long key2) {
        this.key1 = key1;
        this.key2 = key2;
    }
    
    /**
     * Create keypair from array.
     * 
     * @param keypair: array.
     */
    public Keypair(long[] keypair) {
        this.key1 = keypair[0];
        this.key2 = keypair[1];
    }

    /**
     * Generate a hashcode.
     * 
     * @return hashcode. 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.key1 ^ (this.key1 >>> 32));
        hash = 97 * hash + (int) (this.key2 ^ (this.key2 >>> 32));
        return hash;
    }

    /**
     * Check if equal.
     * 
     * @param obj: object that should be equal.
     * @return if objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Keypair other = (Keypair) obj;
        if (this.key1 != other.key1) {
            return false;
        }
        if (this.key2 != other.key2) {
            return false;
        }
        return true;
    }
    
    /**
     * Convert keypair to an array of two longs.
     * 
     * @return long array with two entries.
     */
    public long[] toArray(){
        return new long[]{key1, key2};
    }
}
