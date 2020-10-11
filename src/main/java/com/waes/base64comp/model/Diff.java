package com.waes.base64comp.model;

/**
 * The Diff class is to store the difference detail after comparison.
 */
public class Diff {

    /**
     * The offset of the difference.
     */
    int offset;

    /**
     * The length of the difference.
     */
    int length;

    public Diff (int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Diff{" +
                "offset=" + offset +
                ", length=" + length +
                '}';
    }
}
