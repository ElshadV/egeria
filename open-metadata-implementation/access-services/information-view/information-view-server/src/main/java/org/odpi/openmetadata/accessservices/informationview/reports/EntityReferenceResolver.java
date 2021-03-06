/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.BusinessTerm;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.ReportColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupBasedOnDataView;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupBasedOnDatabaseColumn;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupBasedOnReportColumn;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupStrategy;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class EntityReferenceResolver {


    private OMEntityDao omEntityDao;
    private Map<String, LookupStrategy> strategies = new HashMap();
    private LookupBasedOnDatabaseColumn lookupBasedOnDatabaseColumn;
    private LookupBasedOnReportColumn lookupBasedOnReportColumn;
    private LookupBasedOnDataView lookupBasedOnDataView;

    public EntityReferenceResolver(LookupHelper lookupHelper, OMEntityDao omEntityDao) {
        lookupBasedOnDatabaseColumn = new LookupBasedOnDatabaseColumn(lookupHelper);
        lookupBasedOnReportColumn = new LookupBasedOnReportColumn(omEntityDao);
        lookupBasedOnDataView = new LookupBasedOnDataView(omEntityDao);
        this.omEntityDao = omEntityDao;
        buildStrategies();
    }

    private void buildStrategies() {
        strategies.put(DatabaseColumnSource.class.getName(), lookupBasedOnDatabaseColumn);
        strategies.put(ReportColumnSource.class.getName(), lookupBasedOnReportColumn);
        strategies.put(DataViewColumnSource.class.getName(), lookupBasedOnDataView);
    }


    /**
     *
     * @param source - object used to describe the source of the report column
     * @return
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     */
    public String getSourceGuid(Source source) throws UserNotAuthorizedException, FunctionNotSupportedException,
                                                      InvalidParameterException, RepositoryErrorException,
                                                      PropertyErrorException, TypeErrorException, PagingErrorException {
        if (source == null)
            return null;
        if (!StringUtils.isEmpty(source.getGuid())) {
            return source.getGuid();
        }
        String sourceName = source.getClass().getName();
        if (!StringUtils.isEmpty(source.getQualifiedName())) {
            EntityDetail entity = omEntityDao.getEntity(sourceName, source.getQualifiedName(), false);
            return entity.getGUID();
        }
        LookupStrategy strategy = strategies.get(sourceName);
        if (strategy != null) {
            EntityDetail entity = strategy.lookup(source);
            if (entity != null)
                return entity.getGUID();
        }
        return null;
    }


    /**
     *
     * @param businessTerm - object describing the business term
     * @return
     * @throws UserNotAuthorizedException
     * @throws FunctionNotSupportedException
     * @throws InvalidParameterException
     * @throws RepositoryErrorException
     * @throws PropertyErrorException
     * @throws TypeErrorException
     * @throws PagingErrorException
     */
    public String getBusinessTermGuid(BusinessTerm businessTerm) throws UserNotAuthorizedException,
                                                                        FunctionNotSupportedException,
                                                                        InvalidParameterException,
                                                                        RepositoryErrorException,
                                                                        PropertyErrorException, TypeErrorException,
                                                                        PagingErrorException {
        if (businessTerm == null)
            return null;
        if (!StringUtils.isEmpty(businessTerm.getGuid())) {
            return businessTerm.getGuid();
        }

        if (!StringUtils.isEmpty(businessTerm.getQualifiedName())) {
            EntityDetail entity = omEntityDao.getEntity(Constants.BUSINESS_TERM, businessTerm.getQualifiedName(),
                    false);
            return entity.getGUID();
        }
        return null;
    }


}
