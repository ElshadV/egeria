/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.auditlog;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * The GovernanceConsumerAuditLog is used to define the message content for the OMRS Audit Log.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>Log Message Id - to uniquely identify the message</li>
 * <li>Severity - is this an event, decision, action, error or exception</li>
 * <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 * <li>SystemAction - describes the result of the situation</li>
 * <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum GovernanceEngineAuditCode {
    SERVICE_INITIALIZING("OMAS-GOVERNANCE-ENGINE-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is initializing",
            "The local server has started up a new instance of the Governance Engine OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_REGISTERED_WITH_TOPIC("OMAS-GOVERNANCE-ENGINE-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is registering a listener with the OMRS Topic for server instance {0}",
            "The Governance Engine OMAS is registering the server instance to receive events from the connected open metadata repositories.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INITIALIZED("OMAS-GOVERNANCE-ENGINE-0003",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) has initialized a new instance for server {0}",
            "The Governance Engine OMAS has completed initialization of a new instance.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_TERMINATING("OMAS-GOVERNANCE-ENGINE-0004",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) is shutting down server instance {0}",
            "The local handlers has requested shut down of the Governance Engine OMAS.",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_SHUTDOWN("OMAS-GOVERNANCE-ENGINE-0005",
            OMRSAuditLogRecordSeverity.INFO,
            "The Governance Engine Open Metadata Access Service (OMAS) has completed shutdown of server instance {0}",
            "The requested shutdown has now been processed",
            "No action is required.  This is part of the normal operation of the service."),

    SERVICE_INSTANCE_FAILURE("OMAS-GOVERNANCE-ENGINE-0006",
            OMRSAuditLogRecordSeverity.ERROR,
            "The Governance Engine Open Metadata Access Service (OMAS) is unable to initialize a new instance; error message is {0}",
            "The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.",
            "Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server."),
    ERROR_INITIALIZING_TOPIC_CONNECTION("OMAS-GOVERNANCE-ENGINE-0007",
            OMRSAuditLogRecordSeverity.EXCEPTION,
            "Unable to initialize the Governance Engice Open Metadata Access Service (OMAS) topic connection {0} for server instance {1}; error message was: {2}",
            "The connection could not be initialized.",
            "Review the exception and resolve the configuration. ");

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineAuditCode.class);
    private String logMessageId;
    private OMRSAuditLogRecordSeverity severity;
    private String logMessage;
    private String systemAction;
    private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
    GovernanceEngineAuditCode(String messageId,
                              OMRSAuditLogRecordSeverity severity,
                              String message,
                              String systemAction,
                              String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getLogMessageId() {
        return logMessageId;
    }


    /**
     * Return the severity of the audit log record.
     *
     * @return OMRSAuditLogRecordSeverity enum
     */
    public OMRSAuditLogRecordSeverity getSeverity() {
        return severity;
    }

    /**
     * Returns the log message with the placeholders filled out with the supplied parameters.
     *
     * @param params - strings that plug into the placeholders in the logMessage
     * @return logMessage (formatted with supplied parameters)
     */
    public String getFormattedLogMessage(String... params) {
        MessageFormat mf = new MessageFormat(logMessage);

        return mf.format(params);
    }


    /**
     * Returns a description of the action taken by the system when the condition that caused this exception was
     * detected.
     *
     * @return systemAction String
     */
    public String getSystemAction() {
        return systemAction;
    }


    /**
     * Returns instructions of how to resolve the issue reported in this exception.
     *
     * @return userAction String
     */
    public String getUserAction() {
        return userAction;
    }
}
