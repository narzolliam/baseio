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
package com.generallycloud.test.io.protobuf;

import com.generallycloud.baseio.codec.protobase.ProtobaseCodec;
import com.generallycloud.baseio.codec.protobase.ProtobaseFuture;
import com.generallycloud.baseio.codec.protobase.ProtobaseFutureImpl;
import com.generallycloud.baseio.codec.protobuf.ProtobufUtil;
import com.generallycloud.baseio.common.CloseUtil;
import com.generallycloud.baseio.common.ThreadUtil;
import com.generallycloud.baseio.component.ChannelConnector;
import com.generallycloud.baseio.component.ChannelContext;
import com.generallycloud.baseio.component.IoEventHandleAdaptor;
import com.generallycloud.baseio.component.LoggerSocketSEListener;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.configuration.Configuration;
import com.generallycloud.baseio.protocol.Future;
import com.generallycloud.test.io.protobuf.TestProtoBufBean.SearchRequest;
import com.generallycloud.test.io.protobuf.TestProtoBufBean.SearchRequest.Corpus;
import com.google.protobuf.ByteString;

public class TestProtobufClient {

    public static void main(String[] args) throws Exception {

        ProtobufUtil protobufUtil = new ProtobufUtil();

        protobufUtil.regist(SearchRequest.getDefaultInstance());

        IoEventHandleAdaptor eventHandleAdaptor = new IoEventHandleAdaptor() {

            @Override
            public void accept(SocketSession session, Future future) throws Exception {

                ProtobaseFuture f = (ProtobaseFuture) future;

                SearchRequest res = (SearchRequest) protobufUtil.getMessage(f);

                System.out.println();
                System.out.println("________" + res);
                System.out.println();
            }
        };

        ChannelContext context = new ChannelContext(new Configuration(8300));

        ChannelConnector connector = new ChannelConnector(context);

        context.setIoEventHandle(eventHandleAdaptor);

        context.addSessionEventListener(new LoggerSocketSEListener());

        //		context.addSessionEventListener(new SessionActiveSEListener());

        //		context.setBeatFutureFactory(new FLBeatFutureFactory());

        context.setProtocolCodec(new ProtobaseCodec());

        SocketSession session = connector.connect();

        ProtobaseFuture f = new ProtobaseFutureImpl();

        ByteString byteString = ByteString.copyFrom("222".getBytes());

        SearchRequest request = SearchRequest.newBuilder().setCorpus(Corpus.IMAGES)
                .setPageNumber(100).setQuery("test").setQueryBytes(byteString).setResultPerPage(-1)
                .build();

        protobufUtil.writeProtobuf(request, f);

        session.flush(f);

        ThreadUtil.sleep(100);

        CloseUtil.close(connector);

    }
}
