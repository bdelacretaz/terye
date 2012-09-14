This is the initial description of the Terye project, as of September 2012.

# Background

The Java Content Repository API (JSR-283 standard, usually called JCR) provides a rich set of services for content-based applications such as storage, security, observation and versioning.

JCR is sometimes referred to as a "file system on steroids", as it combines the simplicity of file system storage with the rich feature set of semi-structured databases, benefitting from their content-oriented functionalities. The JCR storage model is an arbitrarily big tree of nodes and properties. Property types that range from integer, boolean, strings and other simple types to large binaries that can store videos or other types of files. Being able to store heterogeneous content with a consistent API is a great benefit for application developers, but puts the scalability and performance burden on the JCR implementations, which also have to cover a much larger spectrum of use cases than column-oriented databases.

The typical use case for Terye is as a backend for content storage in Apache Sling (http://sling.apache.org/).  Based on that project's “everything is content” approach, the repository stores not only user content, but also content rendering scripts, binary code modules and configurations. By default, Sling currently uses Apache Jackrabbit as its JCR repository, which is the reference implementation of JCR and implements all its critical functions from scratch (besides search which is covered by the Apache Lucene library).

Newer semi-structured open source databases, indexing, messaging and clustering products such as CouchDB, MongoDB, HBase, Apache Solr, ActiveMQ and Zookeeper, among others, provide many components that can in theory be assembled to create a JCR repository. Achieving this would allow users to mix and match their favorite technologies to implement their content repository, and allow people who already know these tools to use them with a JCR facade, leveraging the JCR simple, rich and consistent API.

Previous experiments have shown that implementing JCR in this way is not trivial. For example, getting functions like real-time observation of content changes to work efficiently becomes complex with a heavily decoupled system such as the one envisioned. Looking at this from an experimental point of view, and concentrating on the most often used parts of the JCR API, should help us obtain meaningful results, even if the resulting implementation is not fully compliant with the JCR specification, or if some of its parts are not ready for industrial use.

In summary, implementing a fully compliant JCR implementation based on existing components might not be fully possible today. This project aims to find out how far we can get, and analyze the roadblocks that will probably be encountered in a way that helps us achieve this goal in the future.

# Goals
The ideal goal is to create a robust and performant content repository that is fully compliant with the JCR specification, from off-the-shelf software components.

As this goal might not be reachable in the scope of this project, an evolutionary approach is suggested.

We'll start by defining which parts of the API are most important for us, to define priorities as to what needs to be implemented in the early prototypes.

The student will then implement a series of prototypes based on the suggested back-end tools, or other suitable ones, covering those parts of the API that have been defined as having priority.

We will then evaluate those prototypes, based mostly on the JCR test suite and performance benchmarks, and discuss the results with the student to decide in which direction to evolve those prototypes towards the ideal goal.

For parts of the API that are hard to implement in this way, the student will have to analyze the problems to explain what's blocking progress and suggest possible solutions.

All prototypes will be kept in the project's source code repository, so as to be able to go back to them later if needed.
