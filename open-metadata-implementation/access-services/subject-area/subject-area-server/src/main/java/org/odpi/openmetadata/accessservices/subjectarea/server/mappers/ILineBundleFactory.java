/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;

/**
 * The Line bundle is an object that contains the mapper and the name of a Line. This Factory gets a Line bundle based on the classname of
 * a line.
 */
public class ILineBundleFactory
{
    private final OMRSAPIHelper omrsapiHelper;

    public ILineBundleFactory(OMRSAPIHelper omrsapiHelper) {
        this.omrsapiHelper =  omrsapiHelper;
    }

    /**
     * Get an instance of a LineBundle using the class name of the Line.
     * @param bundleName the classname of the Line
     * @return the LineBundle
     */
    public ILineBundle getInstance(String bundleName) {
        ILineBundle bundle =null;
        if (bundleName.equals(Synonym.class.getName())){
            bundle = new LineBundle(
                    new SynonymMapper(omrsapiHelper),
                    bundleName,
                    "Synonym");
        } else if (bundleName.equals(Antonym.class.getName())){
            bundle = new LineBundle(
                    new AntonymMapper(omrsapiHelper),
                    bundleName,
                    "Antonym");
        } else  if (bundleName.equals(TermCategorizationRelationship.class.getName())){
            bundle = new LineBundle(
                    new TermCategorizationMapper(omrsapiHelper),
                    bundleName,
                    "TermCategorization");
        } else if (bundleName.equals(CategoryAnchorRelationship.class.getName())){
            bundle = new LineBundle(
                    new CategoryAnchorMapper(omrsapiHelper),
                    bundleName,
                    "CategoryAnchor");
        } else if (bundleName.equals(TermAnchorRelationship.class.getName())){
            bundle = new LineBundle(
                    new TermAnchorMapper(omrsapiHelper),
                    bundleName,
                    "TermAnchor");
        } else if (bundleName.equals(TermTYPEDBYRelationship.class.getName())){
            bundle = new LineBundle(
                    new TermTYPEDBYRelationshipMapper(omrsapiHelper),
                    bundleName,
                    "TermTYPEDBYRelationship");
        } else if (bundleName.equals(ISARelationship.class.getName())){
            bundle = new LineBundle(
                    new ISARelationshipMapper(omrsapiHelper),
                    bundleName,"ISARelationship"
                    );
        } else if (bundleName.equals(PreferredTerm.class.getName())){
            bundle = new LineBundle(
                    new PreferredTermMapper(omrsapiHelper),
                    bundleName,"PreferredTerm");
        } else if (bundleName.equals(RelatedTerm.class.getName())){
            bundle = new LineBundle(
                    new RelatedTermMapper(omrsapiHelper),
                    bundleName,
                    "RelatedTerm");
        } else if (bundleName.equals(ReplacementTerm.class.getName())){
            bundle = new LineBundle(
                    new ReplacementTermMapper(omrsapiHelper),
                    bundleName,
                    "ReplacementTerm");
        } else if (bundleName.equals(SemanticAssignment.class.getName())){
            bundle = new LineBundle(
                    new SemanticAssignmentMapper(omrsapiHelper),
                    bundleName,
                    "SemanticAssignment");

        } else if (bundleName.equals(TermCategorizationRelationship.class.getName())){
            bundle = new LineBundle(
                    new TermCategorizationMapper(omrsapiHelper),
                    bundleName,
                    "TermCategorization");
        } else if (bundleName.equals(TermHASARelationship.class.getName())){
            bundle = new LineBundle(
                    new TermHASARelationshipMapper(omrsapiHelper),
                    bundleName,
                    "TermHASARelationship");
        } else if (bundleName.equals(ISARelationship.class.getName())){
            bundle = new LineBundle(
                    new ISARelationshipMapper(omrsapiHelper),
                    bundleName,
                    "TermISARelationship"
                    );
        } else if (bundleName.equals(TermISATypeOFRelationship.class.getName())){
            bundle = new LineBundle(
                    new TermISATypeOFRelationshipMapper(omrsapiHelper),
                    bundleName,
                    "TermISATypeOFRelationship");
        }    else if (bundleName.equals(Translation.class.getName()))
        {
            bundle = new LineBundle(
                    new TranslationMapper(omrsapiHelper),
                    bundleName,
                    "Translation");
        } else if (bundleName.equals(UsedInContext.class.getName()))
        {
            bundle = new LineBundle(
                    new UsedInContextMapper(omrsapiHelper), bundleName,"UsedInContext");

        } else if (bundleName.equals(ValidValue.class.getName()))
        {
            bundle = new LineBundle(
                    new ValidValueMapper(omrsapiHelper), bundleName,"ValidValue");
        }
        return bundle;
    }
}