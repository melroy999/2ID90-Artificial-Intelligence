/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20.transposition;

/**
 *
 * @author Melroy
 */
public class Keypair {
    public final long key1;
    public final long key2;

    public Keypair(long key1, long key2) {
        this.key1 = key1;
        this.key2 = key2;
    }
    
    public Keypair(long[] keypair) {
        this.key1 = keypair[0];
        this.key2 = keypair[1];
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.key1 ^ (this.key1 >>> 32));
        hash = 97 * hash + (int) (this.key2 ^ (this.key2 >>> 32));
        return hash;
    }

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
    
    public long[] toArray(){
        return new long[]{key1, key2};
    }
}
