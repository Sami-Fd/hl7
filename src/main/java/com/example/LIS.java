package com.example;

// import ca.uhn.fhir.context.FhirContext;
import com.example.services.hl7Services;
import com.fazecast.jSerialComm.SerialPort;

public class LIS {

    public static void main(String[] args) {

        // Find the COM port for the analyzer
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPort analyzerPort = null;
        for (SerialPort port : ports) {
            if (port.getSystemPortName().equals("COM3")) { // Replace "COM3" with the actual COM port name
                analyzerPort = port;
                break;
            }
        }

        // Check if the analyzer port is found
        if (analyzerPort != null) {
            // Open the serial port
            if (analyzerPort.openPort()) {
                System.out.println("Connected to analyzer on port " + analyzerPort.getSystemPortName());

                // handle the message to the analyzer over the serial port
                hl7Services hl7 = new hl7Services();
                try {
                    // get the message from the hl7Services class
                    String encodedMessage = hl7.main(
                            "ADT", "A01", "P", "TestSendingFacility", "1234567890", "Doe", "John", "123");

                    // send the message to the analyzer over the serial port
                    analyzerPort.writeBytes(encodedMessage.getBytes(), encodedMessage.getBytes().length);

                    // decode the message from the analyzer
                    hl7.decodeMessage(encodedMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Close the serial port when done
                analyzerPort.closePort();
            } else {
                System.err.println("Failed to open port " + analyzerPort.getSystemPortName());
            }
        } else {
            System.err.println("Analyzer port not found.");
        }
    }
}

// Send the HL7 message to the analyzer over the serial port

// // Create a patient resource (replace with actual patient data)
// Patient patient = new Patient();
// patient.addIdentifier().setSystem("http://example.com/patient").setValue("123");
// patient.addName().addFamily("Doe").addGiven("John");

// // Create a FHIR Bundle containing the patient resource
// Bundle bundle = new Bundle();
// bundle.addEntry().setResource(patient);

// // Send the FHIR Bundle to the analyzer over the serial port
// String hl7Message = ctx.newJsonParser().encodeResourceToString(bundle);
// analyzerPort.writeBytes(hl7Message.getBytes(), hl7Message.getBytes().length);