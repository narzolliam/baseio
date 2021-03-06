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
package com.generallycloud.test.io.reconnect;

import com.generallycloud.baseio.codec.fixedlength.FixedLengthCodec;
import com.generallycloud.baseio.common.CloseUtil;
import com.generallycloud.baseio.common.ThreadUtil;
import com.generallycloud.baseio.component.ChannelContext;
import com.generallycloud.baseio.component.IoEventHandleAdaptor;
import com.generallycloud.baseio.component.LoggerSocketSEListener;
import com.generallycloud.baseio.component.ReConnector;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.configuration.Configuration;
import com.generallycloud.baseio.protocol.Future;

public class TestReconnectClient {

    public static void main(String[] args) throws Exception {

        IoEventHandleAdaptor eventHandleAdaptor = new IoEventHandleAdaptor() {

            @Override
            public void accept(SocketSession session, Future future) throws Exception {

            }
        };

        ChannelContext context = new ChannelContext(new Configuration("localhost", 8300));

        ReConnector connector = new ReConnector(context);

        connector.setRetryTime(5000);

        context.setIoEventHandle(eventHandleAdaptor);

        context.addSessionEventListener(new LoggerSocketSEListener());

        context.setProtocolCodec(new FixedLengthCodec());

        //		context.addSessionEventListener(new CloseConnectorSEListener(connector.getRealConnector()));

        connector.connect();

        ThreadUtil.sleep(Long.MAX_VALUE);

        CloseUtil.close(connector);
    }
}
