package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;
import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.io.EzyMath;

public class MsgPackSimpleDeserializer2Test extends MsgPackCodecTest {

	@Test
	public void test1() throws IOException {
		byte[] bin32 = new byte[EzyMath.bin2int(17)];
		Arrays.fill(bin32, (byte)'c');
		String str32 = new String(bin32);
		EzyObjectBuilder dataBuilder = newObjectBuilder()
				.append("k", str32);
		EzyArray request = newArrayBuilder()
				.append(15)
				.append(26)
				.append("abcdef")
				.append(dataBuilder)
				.build();
		byte[] bytes = msgPack.write(request);
		MsgPackSimpleDeserializer deserializer = new MsgPackSimpleDeserializer();
		EzyArray answer = deserializer.deserialize(bytes);
		getLogger().info("answer = {}", answer);
		int appId = answer.get(0);
		int command = answer.get(1);
		String token = answer.get(2);
		assert appId == 15 : "deserialize error";
		assert command == 26 : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
		assert token.equals("abcdef") : "deserialize error";
	}
	
}
