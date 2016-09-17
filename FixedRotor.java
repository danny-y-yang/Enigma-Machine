package enigma;

/** Class that represents a rotor that has no ratchet and does not advance.
 */
class FixedRotor extends Rotor {

    FixedRotor(String rotortype) {
        super(rotortype);
    }

    @Override
    boolean advances() {
        return false;
    }

    @Override
    boolean atNotch() {
        return false;
    }

    /** Fixed rotors do not advance. */
    @Override
    void advance() {
    }

}
