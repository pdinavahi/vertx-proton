/*
* Copyright 2016 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
 * = Vert.x Proton
 *
 * This component facilitates AMQP integrations for Vert.x by providing a thin wrapper around the
 * link:http://qpid.apache.org/[Apache Qpid] Proton AMQP 1.0 protocol engine.
 *
 * WARNING: this module has the tech preview status, this means the API can change between versions. It also
 *          exposes Proton classes directly because it is not an abstraction layer of an AMQP client, it is rather
 *          an integration layer to make Proton integrated with Vert.x and its threading model as well as
 *          networking layer.
 *
 * == Using Vert.x Proton
 *
 * To use Vert.x Proton, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * === Creating a connection
 *
 * Here is an example of connecting and then opening a connection, which can then be used to create senders and
 * receivers.
 *
 * [source,java]
 * ----
 * {@link examples.VertxProtonExamples#example1}
 * ----
 *
 * === Creating a sender
 * 
 * Here is an example of creating a sender and sending a message with it. The onUpdated handler provided in the send
 * call is invoked when disposition updates are received for the delivery, with the example using this to print the
 * delivery state and whether the delivery was settled.
 *
 * [source,java]
 * ----
 * {@link examples.VertxProtonExamples#example2}
 * ----
 * 
 * === Creating a receiver
 * 
 * Here is an example of creating a receiver, and setting a message handler to process the incoming messages and their
 * related delivery.
 *
 * [source,java]
 * ----
 * {@link examples.VertxProtonExamples#example3}
 * ----
 *
 * === Threading Considerations
 *
 * The Proton protocol engine is inherently single threaded, so a given engine and any related connection etc objects
 * must only be used by a single thread at a time. To satisfy this, vertx-proton requires that a connection and related
 * objects are only used by a single Vert.x Context, the one associated with the underlying socket during creation of
 * the connection, with result that only a single thread uses the protocol engine.
 *
 * In the case of a ProtonClient connection, this is always the Context used (or created automatically if there wasn't
 * one present) while calling the connect() methods. The connect handler, and any subsequent callbacks for the related
 * connection, will be fired using this context. The application must use the same context to interact with the
 * connection and related objects outwith any callbacks.
 *
 * For example, consider the following code:
 *
 * [source,java]
 * ----
 * {@link examples.VertxProtonExamples#example4}
 * ----
 *
 * If the above were to be run within a Verticle initialisation for example, then the verticle Context would be used by
 * the connection for I/O and any callbacks, as would calls by the verticle to interact with the connection outwith
 * such callbacks.
 *
 * If however the above snippet were run outwith a Verticle, e.g. embedded in a simple main() method, then no Context
 * would be present during the connect() call and a new one would have to be automatically created during the call. This
 * Context would then be used for the connect callback and any subsequent callbacks. The application code would need to
 * ensure it also used this context to run any interactions with the connection outside of callbacks. One option would
 * be to capture the context in a callback as shown above and then later use it to run tasks. Another would be to
 * explicitly create a Context if needed before calling connect, and then run the connect() operation on it, thus
 * ensuring it becomes the Context associated with the connection. This can be seen in the example below:
 *
 * [source,java]
 * ----
 * {@link examples.VertxProtonExamples#example5}
 * ----
 *
 * In the case of a ProtonServer connection, a Context is associated with the underlying socket for the connection when
 * the server accepts it. This will be used in any callbacks such as those when the connection initially opens and later
 * when sessions etc are opened on it and used. Any usage of a given connection outside of its own callbacks, including
 * any cross-connection interactions, should again be ensured to run on the connections own Context as outlined above.
 */
@Document(fileName = "index.adoc")
package io.vertx.proton;

import io.vertx.docgen.Document;
