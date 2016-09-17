
package enigma;

class Machine {

    private Rotor[] rotors;


    Machine() {
        rotors = new Rotor[5];
    }

    /**
     * Set my rotors to (from left to right) ROTORS.  Initially, the rotor
     * settings are all 'A'.
     */
    void replaceRotors(Rotor[] newrotors) {
        rotors = newrotors;
    }

    /**
     * Set my rotors according to SETTING, which must be a string of four
     * upper-case letters. The first letter refers to the leftmost
     * rotor setting.
     */
    void setRotors(String setting) {

        /* Errors if setting is of the wrong length */
        if (setting.length() != 4) {
            throw new EnigmaException();
        }
        for (int i = 0; i < setting.length(); i++) {
            /* Errors if the input setting has anything but an uppercase letter */
            if (!Character.isUpperCase(setting.charAt(i))) {
                throw new EnigmaException();
            }

            /* convert letter into number (0..25) */
            int newSetting = Rotor.toIndex(setting.charAt(i));

            /* set new setting for the rotor at index i of the rotor array */
            rotors[i + 1].set(newSetting);
        }

    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {

        /* New string to be returned */

        String newMsg = "";

        /* Convert each letter in the string */
        for (int i = 0; i < msg.length(); i++) {


            /* Errors if there are non-alphabetic characters */
            if (!Character.isLetter(msg.charAt(i))) {
                throw new EnigmaException();
            }

            // Advance rotors if necessary
            if (rotors[3].atNotch()) {
                rotors[2].advance();
                rotors[3].advance();
                rotors[4].advance();
            } else if (rotors[4].atNotch() && !rotors[3].atNotch()) {
                rotors[3].advance();
                rotors[4].advance();
            } else if (!rotors[4].atNotch() && !rotors[3].atNotch()) {
                rotors[4].advance();
            }

            //Forward Conversion of letter

            int currentPosition = Rotor.toIndex(msg.charAt(i));

            for (int rotornum = 4; rotornum >= 0; rotornum--) {

                /* sets current rotor */
                Rotor currentRotor = rotors[rotornum];
                int currentSetting = currentRotor.getSetting();

                // enters at contact p + k mod 26
                int enter = currentPosition + currentSetting % 26;
                if (enter > 25) {
                    enter = (currentPosition + currentSetting) % 26;
                }

                int converted = currentRotor.convertForward(enter);

                // exits at c - k mod 26
                int exit = converted - currentSetting % 26;

                if (exit < 0) {
                    exit = 26 + exit;
                }
                currentPosition = exit;

            }

            /* Inverse Conversion of letter */
            for (int rotornum = 1; rotornum < 5; rotornum++) {

                Rotor currentRotor = this.getRotors()[rotornum];
                int currentSetting = currentRotor.getSetting();

                int enter = currentPosition + currentSetting % 26;
                if (enter > 25) {
                    enter = (currentPosition + currentSetting) % 26;
                }

                // Converts backwards instead of forwards
                int converted = currentRotor.convertBackward(enter);
                int exit = converted - currentSetting % 26;

                if (exit < 0) {
                    exit = 26 + exit;
                }
                currentPosition = exit;


            }

            // Adds new letter to the final message
            newMsg += Rotor.toLetter(currentPosition);
        }
        return newMsg;
    }


    Rotor[] getRotors() {
        return rotors;
    }

    // Method that checks if there is a duplicate rotor in the initial configuration
    public boolean checkDuplicateRotors(Rotor[] input) {
        for (int i = 0; i < input.length; i++) {
            Rotor currRotor = input[i];
            for (int j = i + 1; j < input.length; j++) {
                if (input[j] == currRotor) {
                    return true;
                }
            }
        }
        return false;
    }
}
