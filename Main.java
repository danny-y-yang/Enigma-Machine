
package enigma;

import java.io.*;

public final class Main {
    public static void main(String[] args) {
        Machine M;
        BufferedReader input = null;
        try {
            input = new BufferedReader(
                    new InputStreamReader(new FileInputStream(args[0])));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file found.");
        }

        String outputFilename;
        if (args.length >= 2) {
            outputFilename = args[1];
        } else {
            outputFilename = "output.txt";
        }


        M = null;

        try {
            /* Checks if configuration line has been reached, or errors if there is none */
            boolean configured = false;

            while (true) {
                String line = input.readLine(); /* Reached end of a message, or portion of it */
                if (line == null) {
                    break;
                }

                /* Reached the initial configuration of a machine */
                if (isConfigurationLine(line)) {
                    M = new Machine();
                    configure(M, line);
                    configured = true;
                } else {
                    if (!configured) {
                        throw new EnigmaException();
                    }
                    writeMessageLine(M.convert(standardize(line)),
                                     outputFilename);
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        } catch (NullPointerException e) {
            throw new EnigmaException();
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    private static boolean isConfigurationLine(String line) {

        if (line.startsWith("*")) {
            return true;
        }
        return false;
    }

    /** Configure M according to the specification given on CONFIG,
     *  which must have the format specified in the assignment. */
    private static void configure(Machine M, String config) {


        // Create a string array that will contain the configurations to put into machine
        String[] toConfig = new String[6];
        for (int i = 0; i < 6; i++) {
            toConfig[i] = "";
        }

        // configIndex keeps track of what rotor or setting the array is at
        int configIndex = -2;
        for (int i = 0; i < config.length(); i++) {

            /* Errors if there are too many arguments in config */
            if (configIndex > 5) {
                throw new EnigmaException();
            }


            char currentChar = config.charAt(i);

            /* After each whitespace, the next rotor or setting is determined */
            if (Character.isWhitespace(currentChar) || currentChar == '*') {
                configIndex++;
                continue;
            }

            /* Determine the rotor to configure */
            toConfig[configIndex] += currentChar;
        }


        // Errors if the first rotor is not a reflector
        if (!(toConfig[0].equals("B") || toConfig[0].equals("C"))) {
            throw new EnigmaException();
        }

        /* Errors if there are too few arguments */
        if (configIndex < 5) {
            throw new EnigmaException();
        }


        // Rotor array to be inputted into the Machine constructor
        Rotor[] rotorConfig = {
            new Reflector(toConfig[0]),
            new FixedRotor(toConfig[1]),
            new Rotor(toConfig[2]),
            new Rotor(toConfig[3]),
            new Rotor(toConfig[4]),
        };

        /* Errors if there is a duplicate rotor in the config */
        if (M.checkDuplicateRotors(rotorConfig)) {
            throw new EnigmaException();
        }


        M.replaceRotors(rotorConfig);
        M.setRotors(toConfig[5]);

    }


    /** Return the result of converting LINE to all upper case,
     *  removing all blanks and tabs.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    private static String standardize(String line) {

        line.trim();
        String standardized = "";

        /* Create a string array, with each entry being a word */
        String[] words = line.split("\\s+");

        /* Concatenates each word in the array, removing all spaces */

        for (int i = 0; i < words.length; i++) {
            standardized += words[i];
        }

        standardized = standardized.toUpperCase();
        return standardized;

    }



    /** Write MSG in groups of five to out file (except that the last
     *  group may have fewer letters). */
    private static void writeMessageLine(String msg, String filename) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            String outputString = ""; 
            for (int i = 0; i < msg.length(); i += 5) {
                outputString += msg.substring(i, Math.min(i + 5, msg.length()));
                if (i + 5 < msg.length()) {
                    outputString += " ";
                }
            }

            writer.write(outputString + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException when writing: " + e);
        }
    }


}

