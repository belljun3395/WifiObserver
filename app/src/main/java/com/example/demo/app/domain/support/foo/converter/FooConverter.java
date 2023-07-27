package com.example.demo.app.domain.support.foo.converter;

import com.example.demo.app.domain.dto.request.FooDomainRequest;
import com.example.demo.app.domain.dto.response.FooDomainResponse;
import com.example.demo.app.domain.model.Foo;
import com.example.demo.data.mysql.entity.FooEntity;
import org.springframework.stereotype.Component;

@Component
public class FooConverter {

	public Foo from(FooDomainRequest source) {
		return Foo.builder().name(source.getName()).build();
	}

	public FooDomainResponse toDomainResponse(Foo source) {
		return FooDomainResponse.builder().name(source.getName()).build();
	}

	public FooEntity toEntity(Foo source) {
		return FooEntity.builder().build();
	}
}
