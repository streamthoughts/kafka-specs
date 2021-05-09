/*
 * Copyright 2020 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.specs;

import io.streamthoughts.kafka.specs.acl.AclGroupPolicy;
import io.streamthoughts.kafka.specs.acl.AclUserPolicy;
import io.streamthoughts.kafka.specs.resources.BrokerResource;
import io.streamthoughts.kafka.specs.resources.Named;
import io.streamthoughts.kafka.specs.resources.TopicResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The Kafka cluster specification.
 */
public class ClusterSpec implements Serializable {

    private Map<String, TopicResource> topics;

    private final Map<String, AclGroupPolicy> aclGroupPolicies;

    private final Collection<AclUserPolicy> aclUsers;

    private final Collection<BrokerResource> brokers;

    /**
     * Creates a new {@link ClusterSpec} instance.
     */
    public static ClusterSpec withTopics(final Collection<TopicResource> topics) {
        return new ClusterSpec(Collections.emptyList(), topics, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Creates a new {@link ClusterSpec} instance.
     */
    public static ClusterSpec withBrokers(final Collection<BrokerResource> brokers) {
        return new ClusterSpec(brokers, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Creates a new {@link ClusterSpec} instance.
     */
    public static ClusterSpec withUserPolicy(final Collection<AclUserPolicy> aclUsers) {
        return new ClusterSpec(Collections.emptyList(), Collections.emptyList(),  Collections.emptyList(), aclUsers);
    }

    /**
     * Creates a new {@link ClusterSpec} instance.
     */
    public ClusterSpec(final Collection<BrokerResource> brokers,
                       final Collection<TopicResource> topics,
                       final Collection<AclGroupPolicy> aclGroupPolicies,
                       final Collection<AclUserPolicy> aclUsers) {
        Objects.requireNonNull(topics, "topics cannot be null");
        Objects.requireNonNull(topics, "aclGroupPolicies cannot be null");
        Objects.requireNonNull(topics, "aclUsers cannot be null");
        this.brokers = brokers;
        this.topics = Named.keyByName(topics);
        this.aclGroupPolicies = Named.keyByName(aclGroupPolicies);
        this.aclUsers = aclUsers;
    }

    public Map<String, AclGroupPolicy> getAclGroupPolicies() {
        return aclGroupPolicies;
    }

    public Collection<AclUserPolicy> getAclUsers() {
        return aclUsers;
    }

    public Collection<TopicResource> getTopics() {
        return new ArrayList<>(topics.values());
    }

    public Collection<BrokerResource> getBrokers() {
        return brokers;
    }

    public Collection<TopicResource> getTopics(final Predicate<TopicResource> predicate) {
        if (predicate == null) return getTopics();
        return topics.values()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Collection<TopicResource> getTopics(Collection<String> filter) {
        if (filter.isEmpty()) return getTopics();
        return topics.values()
                .stream()
                .filter(t -> filter.contains(t.name()))
                .collect(Collectors.toList());
    }

    public void setTopics(Collection<TopicResource> topics) {
        this.topics = topics.stream().collect(Collectors.toMap(TopicResource::name, o -> o));
    }
}
