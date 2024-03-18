package com.example.services;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v23.datatype.XPN;
import ca.uhn.hl7v2.model.v23.message.ADT_A01;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.PID;
import ca.uhn.hl7v2.model.v26.message.ORM_O01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class hl7Services {

    /**
     * @param args
     * @return
     * @throws HL7Exception
     */
    public String main(
            String messageType, String eventType, String processingId, String namespaceId, String sequenceNumber,
            String familyName, String givenName, String patientId)
            throws Exception {

        AbstractMessage message;

        switch (messageType.toUpperCase()) {
            case "ADT":
                message = new ADT_A01();
                break;
            case "ORM":
                message = new ORM_O01();
                break;
            // Add more cases as needed
            default:
                throw new IllegalArgumentException("Unsupported message type: " + messageType);
        }

        message.initQuickstart(messageType, eventType, processingId);

        // Populate the MSH Segment
        MSH mshSegment = (MSH) message.get("MSH");
        mshSegment.getSendingApplication().getNamespaceID().setValue(namespaceId);
        mshSegment.getSequenceNumber().setValue(sequenceNumber);

        // Populate the PID Segment
        PID pid = (PID) message.get("PID");
        XPN patientName = pid.getPatientName();
        patientName.getFamilyName().setValue(familyName);
        patientName.getGivenName().setValue(givenName);
        pid.getPatientIDInternalID(0).getID().setValue(patientId);

        // Return the encoded message
        PipeParser pipeParser = new PipeParser();
        return pipeParser.encode(message);
    }
    // public String main(
    // // args

    // String nameSpaceID, String sequenceNumber, String familyPatientName, String
    // givenName, String patientID)
    // throws Exception {

    // // Create a new ADT message
    // ADT_A01 adt = new ADT_A01();
    // adt.initQuickstart("ADT", "A01", "P");

    // // Populate the MSH Segment
    // MSH mshSegment = adt.getMSH();
    // mshSegment.getSendingApplication().getNamespaceID().setValue(nameSpaceID);
    // mshSegment.getSequenceNumber().setValue(sequenceNumber);

    // // Populate the PID Segment
    // PID pid = adt.getPID();
    // pid.getPatientName().getFamilyName().setValue(familyPatientName);
    // pid.getPatientName().getGivenName().setValue(givenName);
    // pid.getPatientIDInternalID(0).getID().setValue(patientID);

    // // Now, let's encode the message and look at the output
    // HapiContext context = new DefaultHapiContext();
    // Parser parser = context.getPipeParser();
    // String encodedMessage = parser.encode(adt);
    // System.out.println("Printing ER7 Encoded Message:");
    // System.out.println(encodedMessage);

    // /*
    // * In a real situation, of course, many more segments and fields would be
    // * populated
    // */

    // /*
    // * Prints:
    // *
    // * MSH|^~\&|TestSendingSystem||||200701011539||ADT^A01^ADT A01||||123
    // * PID|||123456||Doe^John
    // */

    // // Next, let's use the XML parser to encode as XML
    // // parser = context.getXMLParser();
    // // encodedMessage = parser.encode(adt);
    // // System.out.println("Printing XML Encoded Message:");
    // // System.out.println(encodedMessage);
    // return encodedMessage;
    // }

    // function to decode the message from the analyzer
    public void decodeMessage(String message) throws HL7Exception {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        ADT_A01 adt = (ADT_A01) parser.parse(message);
        System.out.println("Patient Name: " + adt.getPID().getPatientName());
    }

}