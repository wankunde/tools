package com.wankun.tools.protobuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import com.wankun.tools.protobuf.People.Person;

public class TestPeople {
	@Test
	public void testwrite() throws IOException {
		Person.Builder person = Person.newBuilder();
		person.setId(1001);
		person.setName("wankun");
		person.setEmail("wankunde@163.com");
		// 关键，由build生成对象
		Person p1 = person.build();

		FileOutputStream output = new FileOutputStream(new File("data"));
		p1.writeTo(output);
	}

	@Test
	public void testRead() throws IOException {
		FileInputStream input = new FileInputStream(new File("data"));
		Person p2 = Person.parseFrom(input);
		System.out.println(new ToStringBuilder(p2).append("id", p2.getId()).append("name", p2.getName())
				.append("email", p2.getEmail()).toString());
	}

}
