<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# What is an asset?

An asset is either a digital or physical object/property that provides value to the
organization that owns it.  Examples of an asset include:

* Data sources such as databases, files and data feeds.
* IT infrastructure and applications that automate many aspects of an organization's operation.
* Digital services and APIs that provide access to the services offered by the organization.
* Analytical models and processes that differentiate an organization from its competitors or ensure it is operating legally and ethically.
* Buildings and other locations.
* Physical objects that have a unique identity (eg a serial number).

Much governance is centered around an organization's assets since they
represent tangible value.  This involves maintaining information about each asset
and managing events related to the asset in order to keep it
protected and to get the maximum value from it.

Egeria is particularly focused on providing the ability to maintain the
information necessary for managing digital assets and the infrastructure that supports them.
Although it has a flexible model to allow
the definition of asset to be expanded to include a broader range of physical assets.

## Accessing Assets

Egeria provides an open framework for accessing the content of digital assets and the
information about them.
It is called the [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework/README.md)
and it provides specialized connectors (clients) for accessing specific types of Asset
and the information about them.


## APIs and Events for managing Asset information

Egeria's Open Metadata Access Services (OMASs) provide the specialized services for
managing Assets.  Each OMAS focuses on a particular part of the asset lifecycle or
person/tool that is working with the Assets.  For example,

* **[Asset Catalog OMAS](../../../asset-catalog/README.md)** provides a search service for locating Assets.
* **[Asset Consumer OMAS](../../../asset-consumer/README.md)** provides a service for accessing the content of an Asset,
extracting additional information that is known about the Asset and providing feedback about the Asset.
* **[Asset Owner OMAS](../../../asset-owner/README.md)** provides a service for the owner of an Asset
to classify and manage the asset, and understand how it is being used by the organization.
* **[Discovery Engine OMAS](../../../discovery-engine/README.md)** provides a service for adding annotations to an
asset's information that has been determined by specific analysis of the Asset's contents by a discovery service.
* **[Data Platform OMAS](../../../data-platform/README.md)** enables
a data platform (such as a database or file system) to maintain information about the assets stored on the platform.
* **[IT Infrastructure OMAS](../../../it-infrastructure/README.md)** provides
a service for maintaining information about the IT infrastructure owned or used by an organization.
* **[Data Science OMAS](../../../data-science/README.md)** provides a service for maintaining information
about analytical models and related assets such as python notebooks.
* **[Information View OMAS](../../../information-view/README.md)**
enables business intelligence and data virtualization tools to maintain information about the data views and reporting Assets they are maintaining.



## Information about Assets that is managed by Egeria

Egeria's Open Metadata Repository Services (OMRS) provides the ability to store and extract information about
Assets in a distributed collections of servers called an
[open metadata repository cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md).

The types of information that can be stores are defined in the [open metadata types](../../../../../open-metadata-publication/website/open-metadata-types/README.md).

In the open metadata types, there is a common abstract type called
**Asset** that appears in the
**[base model](../../../../../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md)**.

Inheriting from asset is a hierarchy of increasingly specialized definitions
for different types of Assets.  Each definition adds more properties
about the Asset.  Figure 1 shows some of the key assets.

![Figure 1](asset-hierarchy.png)
**Figure 1:** Asset hierarchy

* **Infrastructure** covers physical assets from machinery, sensors and networks.
There is a subclass called **ITInfrastructure** that is where IT hosts and
services ar located.

* **Process** describes processing from simple software components to
multi-task workflows.

* **DataStore** a physical store of data such as a file.

* **DataSet** describes a logical collection of data.  These are created
from a combination of processes and physical data stores.
They tend to be the collections of data that are of interest to the
organization.

* **API** the means to access digital capability consisting of infrastructure,
processes, data stores and data sets.

**[Area 2](../../../../../open-metadata-publication/website/open-metadata-types/Area-2-models.md)** is where the asset definitions are built out.

In addition, model [0205](../../../../../open-metadata-publication/website/open-metadata-types/0205-Connection-Linkage.md) shows how
an Asset is associated with a [Connection](../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object.
The connection object provides the properties necessary to create a connectors to access the asset's contents as described in section
[Accessing Assets](#accessing-assets) above.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.