package com.handmade.tle.prompt.service.store;

import org.chenile.utils.entity.service.EntityStore;
import com.handmade.tle.shared.model.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.base.exception.NotFoundException;
import com.handmade.tle.shared.repository.PromptRepository;
import java.util.Optional;

public class PromptEntityStore implements EntityStore<Prompt>{
    @Autowired private PromptRepository promptRepository;

	@Override
	public void store(Prompt entity) {
        promptRepository.save(entity);
	}

	@Override
	public Prompt retrieve(String id) {
        Optional<Prompt> entity = promptRepository.findById(id);
        if (entity.isPresent()) return entity.get();
        throw new NotFoundException(1500,"Unable to find Prompt with ID " + id);
	}

}
