package com.handmade.tle.prompt.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.shared.dto.ValidatePromptPayload;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 * <p>
 * Use this class if you want to augment the common logic for this specific
 * transition
 * </p>
 * <p>
 * Use a customized payload if required instead of MinimalPayload
 * </p>
 */
public class ValidatePromptAction extends AbstractSTMTransitionAction<Prompt, ValidatePromptPayload> {

	public void transitionTo(Prompt prompt,
			ValidatePromptPayload payload,
			State startState, String eventId,
			State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
		if (payload.score > 0) {
			prompt.validationScore = payload.score;
		} else {
			prompt.validationScore = 1; // Default
		}
	}

}
