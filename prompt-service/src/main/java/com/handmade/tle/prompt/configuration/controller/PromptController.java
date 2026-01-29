package com.handmade.tle.prompt.configuration.controller;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.springframework.http.ResponseEntity;

import org.chenile.stm.StateEntity;

import org.springframework.web.bind.annotation.*;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.handmade.tle.shared.model.Prompt;


@RestController
@ChenileController(value = "promptService", serviceName = "_promptStateEntityService_",
		healthCheckerName = "promptHealthChecker")
public class PromptController extends ControllerSupport{
	
	@GetMapping("/prompt/{id}")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Prompt>>> retrieve(
			HttpServletRequest httpServletRequest,
			@PathVariable String id){
		return process(httpServletRequest,id);
	}

	@PostMapping("/prompt")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Prompt>>> create(
			HttpServletRequest httpServletRequest,
			@ChenileParamType(StateEntity.class)
			@RequestBody Prompt entity){
		return process(httpServletRequest,entity);
	}

	
	@PatchMapping("/prompt/{id}/{eventID}")
	@BodyTypeSelector("promptBodyTypeSelector")
	public ResponseEntity<GenericResponse<StateEntityServiceResponse<Prompt>>> processById(
			HttpServletRequest httpServletRequest,
			@PathVariable String id,
			@PathVariable String eventID,
			@ChenileParamType(Object.class) 
			@RequestBody String eventPayload){
		return process(httpServletRequest,id,eventID,eventPayload);
	}


}
