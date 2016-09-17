// This is a SUGGESTED skeleton file.  Throw it away if you don't use it.
package enigma;


/** Class that represents a rotor in the enigma machine.
 *  @author
 */
class Rotor {
    // This needs other methods, fields, and constructors.
    private String typeofrotor;
    private String notch1;
    private String notch2;

    /** My current setting (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int _setting;


    Rotor(String typeofrotor) {

        this.typeofrotor = typeofrotor;
        this._setting = 0;

        if (this.typeofrotor.equals("I")) {
            notch1 = "Q";
        } else if (this.typeofrotor.equals("II")) {
            notch1 = "E";
        } else if (this.typeofrotor.equals("III")) {
            notch1 = "V";
        } else if (this.typeofrotor.equals("IV")) {
            notch1 = "J";
        } else if (this.typeofrotor.equals("V")) {
            notch1 = "Z";
        } else if (this.typeofrotor.equals("VI")) {
            notch1 = "Z";
            notch2 = "M";
        } else if (this.typeofrotor.equals("VII")) {
            notch1 = "Z";
            notch2 = "M";
        } else if (this.typeofrotor.equals("VIII")) {
            notch1 = "Z";
            notch2 = "M";

            // Errors if the rotor is misnamed
        } else if (!(this.typeofrotor.equals("B")
                || this.typeofrotor.equals("C")
                || this.typeofrotor.equals("BETA")
                || this.typeofrotor.equals("GAMMA"))) {
            throw new EnigmaException();
        }

    }

    /* Initializes an alphabet from A-Z */
    static char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
        'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    /** Size of alphabet used for plaintext and ciphertext. */
    static final int ALPHABET_SIZE = 26;

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        return alphabet[p];
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        int i = 0;
        while (i < alphabet.length) {
            if (alphabet[i] == c) {
                break;
            }
            i++;
        }
        return i;

    }

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return true;
    }

    /** Returns true iff this rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return true;
    }

    /** Return my current rotational setting as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */

    String getTypeofrotor() {
        return typeofrotor;
    }

    String getNotch1() {
        return notch1;
    }

    String getNotch2() {
        return notch2;
    }

    int getSetting() {
        return _setting;
    }

    /** Set getSetting() to POSN.  */
    void set(int posn) {
        assert 0 <= posn && posn < ALPHABET_SIZE;
        _setting = posn;
    }

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {

        // Go through the permutation data to determine which rotor conversion to use,
        // and returns the index (0..25) of P converted.

        int i = 0;
        while (i < PermutationData.ROTOR_SPECS.length) {
            if (PermutationData.ROTOR_SPECS[i][0].equals(this.typeofrotor)) {
                break;
            }
            i++;

        }
        return Rotor.toIndex(PermutationData.ROTOR_SPECS[i][1].charAt(p));
    }






    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {

        // Same as convertForward, but uses the backwards permutation table

        int i = 0;
        while (i < PermutationData.ROTOR_SPECS.length) {
            if (PermutationData.ROTOR_SPECS[i][0].equals(this.typeofrotor)) {
                break;
            }
            i++;

        }
        return Rotor.toIndex(PermutationData.ROTOR_SPECS[i][2].charAt(e));

    }



    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        if (this.notch2 != null) {
            return (this.getSetting() == toIndex(this.notch1.charAt(0))
                    || this.getSetting() == toIndex(this.notch2.charAt(0)));
        }

        return this.getSetting() == toIndex(this.notch1.charAt(0));
    }

    /** Advance me one position. */
    void advance() {
        int currentPosition = this.getSetting();
        set((currentPosition + 1) % 26);
    }

}



