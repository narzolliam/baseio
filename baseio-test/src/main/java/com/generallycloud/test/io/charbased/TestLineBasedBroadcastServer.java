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
package com.generallycloud.test.io.charbased;

import com.generallycloud.baseio.codec.charbased.CharBasedCodec;
import com.generallycloud.baseio.component.ChannelAcceptor;
import com.generallycloud.baseio.component.ChannelContext;
import com.generallycloud.baseio.component.IoEventHandleAdaptor;
import com.generallycloud.baseio.component.LoggerSocketSEListener;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.configuration.Configuration;
import com.generallycloud.baseio.protocol.Future;

public class TestLineBasedBroadcastServer {

    public static void main(String[] args) throws Exception {

        IoEventHandleAdaptor eventHandleAdaptor = new IoEventHandleAdaptor() {

            @Override
            public void accept(SocketSession session, Future future) throws Exception {
                long old = System.currentTimeMillis();
                String res = "hello world!";
                future.write(res, session);
                ChannelAcceptor acceptor = (ChannelAcceptor) session.getContext()
                        .getChannelService();
                acceptor.broadcast(future);
                long now = System.currentTimeMillis();
                System.out.println("广播花费时间：" + (now - old) + ",连接数："
                        + session.getContext().getSessionManager().getManagedSessionSize());
            }
        };

        Configuration configuration = new Configuration();
        configuration.setPort(8300);
        ChannelContext context = new ChannelContext(configuration);
        ChannelAcceptor acceptor = new ChannelAcceptor(context);
        context.addSessionEventListener(new LoggerSocketSEListener());
        context.setIoEventHandle(eventHandleAdaptor);
        context.setProtocolCodec(new CharBasedCodec());
        acceptor.bind();
    }

}
