/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.apache.commons.collections.map.ReferenceMap;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles mapping the majority of IGC objects' attributes to OMRS entity attributes.
 */
public abstract class MainObjectMapper extends ReferenceMapper {

    /**
     * Generic set of properties used by all IGC objects.
     */
    private static final PropertyMappingSet COMMON_PROPERTIES = new PropertyMappingSet() {{
        put("created_by", "::createdBy");
        put("created_on", "::createTime");
        put("modified_by", "::updatedBy");
        put("modified_on", "::updateTime");
    }};

    /**
     * Generic set of properties (potentially) used by all IGC objects to represent classifications.
     * (These are handled specially as IGC does not really have a first-class "Classification" concept;
     *  so these are where a particular implementation overloads meaning into some pre-existing asset
     *  and / or relationship.)
     */
    private static final String[] CLASSIFICATION_ELEMENTS = { };

    /**
     * Generic set of relationships used by all IGC objects.
     */
    private static final RelationshipMappingSet COMMON_RELATIONSHIPS = new RelationshipMappingSet() {{
        put(
                "assigned_to_terms",
                "SemanticAssignment",
                "assignedElements",
                "meaning"
        );
        put(
                "labels",
                "AttachedTag",
                "taggedElement",
                "tags"
        );
    }};

    protected PropertyMappingSet PROPERTIES;
    protected RelationshipMappingSet RELATIONSHIPS;
    protected ArrayList<String> CLASSIFICATION_PROPERTIES;

    public MainObjectMapper(MainObject mainObject,
                            IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                            String userId,
                            String pojoName) {
        super(mainObject, igcomrsRepositoryConnector, userId);
        igcPOJO = ReferenceMapper.IGC_REST_GENERATED_MODEL_PKG + "." + igcomrsRepositoryConnector.getIGCVersion() + "." + pojoName;
        PROPERTIES = new PropertyMappingSet();
        RELATIONSHIPS = new RelationshipMappingSet();
        CLASSIFICATION_PROPERTIES = new ArrayList<>();
    }

    /**
     * Map the IGC entity to an OMRS EntitySummary object.
     *
     * @return EntitySummary
     */
    public EntitySummary getOMRSEntitySummary() {
        mapIGCToOMRSEntitySummary(CLASSIFICATION_PROPERTIES);
        return getSummary();
    }

    /**
     * Map the IGC entity to an OMRS EntityDetail object.
     *
     * @return EntityDetail
     */
    public EntityDetail getOMRSEntityDetail() {
        mapIGCToOMRSEntityDetail(PROPERTIES, CLASSIFICATION_PROPERTIES);
        return getDetail();
    }

    /**
     * Map the IGC entity's relationships to OMRS Relationship objects.
     *
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return List<Relationship>
     */
    public List<Relationship> getOMRSRelationships(String          relationshipTypeGUID,
                                                   int             fromRelationshipElement,
                                                   SequencingOrder sequencingOrder,
                                                   int             pageSize) {
        getMappedRelationships(
                RELATIONSHIPS,
                relationshipTypeGUID,
                fromRelationshipElement,
                sequencingOrder,
                pageSize);
        return getRelationships();
    }

    /**
     * Map the IGC entity's classifications to OMRS Classification objects.
     * (This is called automatically as part of getOMRSEntitySummary and getOMRSEntityDetail)
     *
     * @return List<Classification>
     */
    protected List<Classification> getOMRSClassifications() {
        getMappedClassifications();
        return getClassifications();
    }

    /**
     * Maps the (IGC) object this class was initialised with to an OMRS EntitySummary object, using the provided
     * mappings.
     * @param classificationProperties any IGC properties needed to setup classifications in the OMRS EntityDetail object
     */
    protected void mapIGCToOMRSEntitySummary(List<String> classificationProperties) {

        // Merge together all the properties we want to map
        String[] allProps = ReferenceMapper.concatAll(
                classificationProperties.toArray(new String[0]),
                COMMON_PROPERTIES.getIgcPropertyNames(),
                CLASSIFICATION_ELEMENTS
        );

        // Retrieve the full details we'll require for summary BEFORE handing off to superclass
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);

        // Handle any super-generic mappings first
        super.mapIGCToOMRSEntitySummary(classificationProperties);

        // Then handle MainObject-generic mappings and classifications
        setupEntityObj(summary);

    }

    /**
     * Maps the (IGC) object this class was initialised with to an OMRS EntityDetail object, using the provided
     * mappings.
     *
     * @param propertyMap the property mappings to use in creating the OMRS EntityDetail object
     * @param classificationProperties any IGC properties needed to setup classifications in the OMRS EntityDetail object
     */
    protected void mapIGCToOMRSEntityDetail(PropertyMappingSet propertyMap, List<String> classificationProperties) {

        // Merge the detailed properties together (MainObject and more specific POJO mappings that were passed in)
        String[] allProps = ReferenceMapper.concatAll(
                propertyMap.getIgcPropertyNames(),
                classificationProperties.toArray(new String[0]),
                COMMON_PROPERTIES.getIgcPropertyNames(),
                CLASSIFICATION_ELEMENTS
        );

        // Retrieve only this set of properties for the object (no more, no less)
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);

        // Handle any super-generic mappings first
        super.mapIGCToOMRSEntityDetail(propertyMap, classificationProperties);

        // Then handle any MainObject-generic mappings and classifications
        setupEntityObj(detail);

        // Use reflection to apply POJO-specific mappings
        InstanceProperties instanceProperties = getMappedInstanceProperties(propertyMap);
        detail.setProperties(instanceProperties);

    }

    /**
     * Simple utility function to avoid implementing shared EntitySummary and EntityDetail setup twice.
     *
     * @param omrsObj OMRS object to map into (EntityDetail or EntitySummary)
     */
    private void setupEntityObj(EntitySummary omrsObj) {

        MainObject myself = (MainObject) me;

        omrsObj.setCreatedBy(myself.getCreatedBy());
        omrsObj.setCreateTime(myself.getCreatedOn());
        omrsObj.setUpdatedBy(myself.getModifiedBy());
        omrsObj.setUpdateTime(myself.getModifiedOn());

        // Avoid doing this multiple times: if one has retrieved classifications it'll
        // be the same classifications for the other
        if (classifications == null) {
            classifications = new ArrayList<>();
            getMappedClassifications();
        }

        omrsObj.setClassifications(classifications);

    }

    /**
     * Retrieves the InstanceProperties based on the mappings defined in the provided PropertyMappingSet.
     *
     * @param mappings the mappings to use for retrieving a set of InstanceProperties
     * @return InstanceProperties
     */
    private InstanceProperties getMappedInstanceProperties(PropertyMappingSet mappings) {

        InstanceProperties instanceProperties = new InstanceProperties();
        ClassLoader classLoader = this.getClass().getClassLoader();

        try {

            Class clazz = classLoader.loadClass(igcPOJO);
            Method getPropertyByName = clazz.getMethod("getPropertyByName", String.class);

            // We'll always start by using the Identity string as the qualified name
            Method getIdentity = clazz.getMethod("getIdentity", IGCRestClient.class);
            String qualifiedName = getIdentity.invoke(me, igcomrsRepositoryConnector.getIGCRestClient()).toString();
            instanceProperties.setProperty("qualifiedName", getPrimitivePropertyValue(qualifiedName));

            // Then we'll iterate through the provided mappings to set an OMRS instance property for each one
            for (int i = 0; i < mappings.size(); i++) {
                instanceProperties.setProperty(
                        mappings.get(i).getOmrsEntityAttr(),
                        getPrimitivePropertyValue(getPropertyByName.invoke(me, mappings.get(i).getIgcPropertyName()))
                );
            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return instanceProperties;

    }

    /**
     * Adds any relationships defined through the provided RelationshipMappingSet to the the private 'relationships'
     * member, for use in retrieving via getOMRSRelationships. First looks up any *Mapper-specific mappings to apply,
     * and then iterates through any common relationships we could expect across any IGC object. (This allows *Mapper-
     * specific mappings to effectively "override" any common mappings, in cases where the same relationship in IGC
     * may represent multiple meanings depending on the asset type the relationship is used on.)
     *
     * @param mappings the mappings to use for retrieving the relationships
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     */
    private void getMappedRelationships(RelationshipMappingSet mappings,
                                        String                 relationshipTypeGUID,
                                        int                    fromRelationshipElement,
                                        SequencingOrder        sequencingOrder,
                                        int                    pageSize) {

        // Start off by marking that any properties used for classifications should not be used again
        // for relationships
        addAlreadyMappedProperties(CLASSIFICATION_PROPERTIES);

        // Merge together all the properties we want to map
        String[] allProps = null;

        // TODO: Filter down to only the relationship specified (ie. relationshipTypeGUID != null)
        //if (relationshipTypeGUID == null) {
            allProps = ReferenceMapper.concatAll(
                    COMMON_PROPERTIES.getIgcPropertyNames(),
                    mappings.getIgcPropertyNames(),
                    COMMON_RELATIONSHIPS.getIgcPropertyNames()
            );
        /*} else {

            allProps = ReferenceMapper.concatAll(
                    COMMON_PROPERTIES.getIgcPropertyNames(),
                    "???"
            );
        }*/

        IGCSearchSorting sort = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);

        // TODO: handle multi-page results with different starting points (ie. fromRelationshipElement != 0)

        // Retrieve the full details we'll require for the relationships
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps, pageSize, sort);

        _getMappedRelationships(mappings);
        _getMappedRelationships(COMMON_RELATIONSHIPS);

    }

    /**
     * Utility function that actually does the Relationship object setup and addition to 'relationships' member.
     *
     * @param mappings the mappings to use for retrieving the relationships
     */
    private void _getMappedRelationships(RelationshipMappingSet mappings) {

        ClassLoader classLoader = this.getClass().getClassLoader();
        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();
        List<String> alreadyUsedProperties = getAlreadyMappedProperties();

        try {

            Class clazz = classLoader.loadClass(igcPOJO);
            Method getPropertyByName = clazz.getMethod("getPropertyByName", String.class);

            // Iterate through the provided mappings to create a number of OMRS relationships
            for (int i = 0; i < mappings.size(); i++) {

                String igcRelationshipName = mappings.get(i).getIgcRelationshipName();

                // Only continue if we haven't already handled that relationship
                if (!alreadyUsedProperties.contains(igcRelationshipName)) {

                    String omrsRelationshipName = mappings.get(i).getOmrsRelationshipType();
                    String omrsSourceProperty = mappings.get(i).getOmrsRelationshipSourceProperty();
                    String omrsTargetProperty = mappings.get(i).getOmrsRelationshipTargetProperty();

                    RelationshipDef relationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(SOURCE_NAME, omrsRelationshipName);

                    Object igcRelationshipObj = getPropertyByName.invoke(me, igcRelationshipName);

                    // Handle single instance relationship one way
                    if (igcRelationshipObj != null && Reference.isReference(igcRelationshipObj)) {

                        Reference igcRelationship = (Reference) igcRelationshipObj;

                        _addMappedRelationship(
                                igcRelationshipName,
                                relationshipDef,
                                omrsSourceProperty,
                                omrsTargetProperty,
                                igcRelationship
                        );

                        addAlreadyMappedProperty(igcRelationshipName);

                    } else if (igcRelationshipObj != null && Reference.isReferenceList(igcRelationshipObj)) { // and list of relationships another

                        ReferenceList igcRelationships = (ReferenceList) getPropertyByName.invoke(me, igcRelationshipName);

                        // TODO: paginate rather than always retrieving the full set
                        igcRelationships.getAllPages(igcRestClient);

                        // Iterate through all of the existing IGC relationships of that type to create an OMRS relationship
                        // for each one
                        for (Reference relation : igcRelationships.getItems()) {

                            _addMappedRelationship(
                                    igcRelationshipName,
                                    relationshipDef,
                                    omrsSourceProperty,
                                    omrsTargetProperty,
                                    relation
                            );

                        }

                        addAlreadyMappedProperty(igcRelationshipName);

                    }

                }

            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | RepositoryErrorException e) {
            e.printStackTrace();
        }

    }

    public void _addMappedRelationship(String igcRelationshipName,
                                       RelationshipDef omrsRelationshipDef,
                                       String omrsSourceProperty,
                                       String omrsTargetProperty,
                                       Reference relation) throws RepositoryErrorException {

        final String methodName = "_getMappedRelationships";
        final String repositoryName = igcomrsRepositoryConnector.getRepositoryName();

        Relationship omrsRelationship = new Relationship();

        try {
            InstanceType instanceType = igcomrsRepositoryConnector.getRepositoryHelper().getNewInstanceType(
                    SOURCE_NAME,
                    omrsRelationshipDef
            );
            omrsRelationship.setType(instanceType);
        } catch (TypeErrorException e) {
            e.printStackTrace();
        }

        // Set:
        // - the GUID of the relationship to <source_entity_RID>_<property_name>_<target_entity_RID>
        omrsRelationship.setGUID(me.getId() + "_" + igcRelationshipName + "_" + relation.getId());
        omrsRelationship.setStatus(InstanceStatus.ACTIVE);

        EntityProxy ep1 = null;
        EntityProxy ep2 = null;
        String omrsEndOneProperty = omrsRelationshipDef.getEndDef1().getAttributeName();

        // If end one property matches the OMRS property linked to IGC source, use this object
        if (omrsSourceProperty.equals(omrsEndOneProperty)) {
            ep1 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    me,
                    omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                    userId
            );
            ep2 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    relation,
                    omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                    userId
            );
            if (ep1 != null) {
                // ... and in this case, set the version to the epoch time of the ep1 (source) proxy
                omrsRelationship.setVersion(ep1.getUpdateTime().getTime());
            }
        } else if (omrsTargetProperty.equals(omrsEndOneProperty)) {
            // If end one property matches the OMRS property linked to IGC target, use the relation object
            ep1 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    relation,
                    omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                    userId
            );
            ep2 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    me,
                    omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                    userId
            );
            if (ep2 != null) {
                // ... and in this case, set the version to the epoch time of the ep2 (source) proxy
                omrsRelationship.setVersion(ep2.getUpdateTime().getTime());
            }
        } else {
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_RELATIONSHIP_ENDS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        if (ep1 != null && ep2 != null) {
            omrsRelationship.setEntityOneProxy(ep1);
            omrsRelationship.setEntityTwoProxy(ep2);
            relationships.add(omrsRelationship);
        }

    }

}

