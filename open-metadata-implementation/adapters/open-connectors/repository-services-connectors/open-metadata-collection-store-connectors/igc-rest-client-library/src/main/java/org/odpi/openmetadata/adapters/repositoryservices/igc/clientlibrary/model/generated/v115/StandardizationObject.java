/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'standardization_object' asset type in IGC, displayed as 'Standardization Object' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StandardizationObject extends Reference {

    public static String getIgcTypeId() { return "standardization_object"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'data_quality_specifications' property, displayed as 'Data Quality Specification' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationObject} objects.
     */
    protected ReferenceList data_quality_specifications;

    /**
     * The 'folder' property, displayed as 'Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The 'used_by_stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList used_by_stages;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'fields' property, displayed as 'Data Fields' in the IGC UI.
     */
    protected ArrayList<String> fields;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public ReferenceList getDataQualitySpecifications() { return this.data_quality_specifications; }
    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public void setDataQualitySpecifications(ReferenceList data_quality_specifications) { this.data_quality_specifications = data_quality_specifications; }

    /** @see #folder */ @JsonProperty("folder")  public Reference getFolder() { return this.folder; }
    /** @see #folder */ @JsonProperty("folder")  public void setFolder(Reference folder) { this.folder = folder; }

    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public ReferenceList getUsedByStages() { return this.used_by_stages; }
    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public void setUsedByStages(ReferenceList used_by_stages) { this.used_by_stages = used_by_stages; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #fields */ @JsonProperty("fields")  public ArrayList<String> getFields() { return this.fields; }
    /** @see #fields */ @JsonProperty("fields")  public void setFields(ArrayList<String> fields) { this.fields = fields; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean isStandardizationObject(Object obj) { return (obj.getClass() == StandardizationObject.class); }

}
