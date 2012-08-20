/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.support.single.shard;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ValidateActions;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 *
 */
public abstract class SingleShardOperationRequest implements ActionRequest {

    protected String index;

    private boolean threadedListener = false;
    private boolean threadedOperation = true;

    protected SingleShardOperationRequest() {
    }

    public SingleShardOperationRequest(String index) {
        this.index = index;
    }

    @Override
    public ActionRequestValidationException validate() {
        ActionRequestValidationException validationException = null;
        if (index == null) {
            validationException = ValidateActions.addValidationError("index is missing", validationException);
        }
        return validationException;
    }

    public String index() {
        return index;
    }

    SingleShardOperationRequest index(String index) {
        this.index = index;
        return this;
    }

    /**
     * Should the listener be called on a separate thread if needed.
     */
    @Override
    public boolean listenerThreaded() {
        return threadedListener;
    }

    @Override
    public SingleShardOperationRequest listenerThreaded(boolean threadedListener) {
        this.threadedListener = threadedListener;
        return this;
    }

    /**
     * Controls if the operation will be executed on a separate thread when executed locally.
     */
    public boolean operationThreaded() {
        return threadedOperation;
    }

    /**
     * Controls if the operation will be executed on a separate thread when executed locally.
     */
    public SingleShardOperationRequest operationThreaded(boolean threadedOperation) {
        this.threadedOperation = threadedOperation;
        return this;
    }

    protected void beforeLocalFork() {
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        index = in.readUTF();
        // no need to pass threading over the network, they are always false when coming throw a thread pool
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeUTF(index);
    }

}

