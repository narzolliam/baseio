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
package com.generallycloud.test.io.protobase;

import com.generallycloud.baseio.codec.protobase.ProtobaseCodec;
import com.generallycloud.baseio.codec.protobase.ProtobaseFuture;
import com.generallycloud.baseio.common.CloseUtil;
import com.generallycloud.baseio.component.ChannelConnector;
import com.generallycloud.baseio.component.ChannelContext;
import com.generallycloud.baseio.component.LoggerSocketSEListener;
import com.generallycloud.baseio.configuration.Configuration;
import com.generallycloud.baseio.container.protobase.FixedSession;
import com.generallycloud.baseio.container.protobase.SimpleIoEventHandle;

public class TestException {

    public static void main(String[] args) throws Exception {

        String serviceKey = "TestExceptionServlet";
        String param = "ttt";

        SimpleIoEventHandle eventHandle = new SimpleIoEventHandle();
        Configuration configuration = new Configuration(8300);
        ChannelContext context = new ChannelContext(configuration);
        ChannelConnector connector = new ChannelConnector(context);
        context.setIoEventHandle(eventHandle);
        context.setProtocolCodec(new ProtobaseCodec());
        context.addSessionEventListener(new LoggerSocketSEListener());
        FixedSession session = new FixedSession(connector.connect());
        ProtobaseFuture future = session.request(serviceKey, param);
        System.out.println(future.getReadText());
        CloseUtil.close(connector);
    }

}
