/*
 * Copyright 2015-2017 GenerallyCloud.com
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.generallycloud.baseio.codec.http11;

import java.io.IOException;

import com.generallycloud.baseio.TimeoutException;
import com.generallycloud.baseio.component.ChannelContext;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.concurrent.Waiter;

public class HttpClient {

    private ChannelContext    context;
    private SocketSession     session;
    private HttpIOEventHandle ioEventHandle;

    public HttpClient(SocketSession session) {
        this.session = session;
        this.context = session.getContext();
        this.ioEventHandle = (HttpIOEventHandle) context.getIoEventHandle();
    }

    public synchronized HttpFuture request(HttpFuture future, long timeout) throws IOException {
        Waiter waiter = ioEventHandle.newWaiter();
        session.flush(future);
        if (waiter.await(timeout)) {
            throw new TimeoutException("timeout");
        }
        return (HttpFuture) waiter.getResponse();
    }

    public synchronized HttpFuture request(HttpFuture future) throws IOException {
        return request(future, 3000);
    }

}
