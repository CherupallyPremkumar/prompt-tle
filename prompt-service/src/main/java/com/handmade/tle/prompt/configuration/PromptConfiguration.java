package com.handmade.tle.prompt.configuration;

import java.io.InputStream;

import com.handmade.tle.prompt.service.security.PromptSecurityService;
import org.chenile.security.service.SecurityService;
import org.chenile.stm.*;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.ConfigProvider;
import org.chenile.stm.impl.ConfigProviderImpl;
import org.chenile.stm.impl.*;
import org.chenile.stm.spring.SpringBeanFactoryAdapter;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.chenile.workflow.service.stmcmds.*;
import com.handmade.tle.shared.model.Prompt;
import com.handmade.tle.prompt.service.cmds.*;
import com.handmade.tle.prompt.service.healthcheck.PromptHealthChecker;
import com.handmade.tle.prompt.service.store.PromptEntityStore;
import org.chenile.workflow.api.WorkflowRegistry;
import org.chenile.stm.State;
import org.chenile.workflow.service.activities.ActivityChecker;
import org.chenile.workflow.service.activities.AreActivitiesComplete;

@Configuration
public class PromptConfiguration {
    private static final String FLOW_DEFINITION_FILE = "com/upload/prompt/prompt-states.xml";
    public static final String PREFIX_FOR_PROPERTIES = "Prompt";
    public static final String PREFIX_FOR_RESOLVER = "prompt";

    @Bean
    BeanFactoryAdapter promptBeanFactoryAdapter() {
        return new SpringBeanFactoryAdapter();
    }

    @Bean
    STMFlowStoreImpl promptFlowStore(
            @Qualifier("promptBeanFactoryAdapter") BeanFactoryAdapter promptBeanFactoryAdapter,
            @Qualifier("promptEnablementStrategy") EnablementStrategy enablementStrategy) throws Exception {
        STMFlowStoreImpl stmFlowStore = new STMFlowStoreImpl();
        stmFlowStore.setBeanFactory(promptBeanFactoryAdapter);
        stmFlowStore.setEnablementStrategy(enablementStrategy);
        return stmFlowStore;
    }

    @Bean
    STM<Prompt> promptEntityStm(@Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore) throws Exception {
        STMImpl<Prompt> stm = new STMImpl<>();
        stm.setStmFlowStore(stmFlowStore);
        return stm;
    }

    @Bean
    STMActionsInfoProvider promptActionsInfoProvider(@Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore,
            @Qualifier("promptFlowReader") XmlFlowReader flowReader) {
        STMActionsInfoProvider provider = new STMActionsInfoProvider(stmFlowStore);
        WorkflowRegistry.addSTMActionsInfoProvider("prompt", provider);
        return provider;
    }

    @Bean
    EntityStore<Prompt> promptEntityStore() {
        return new PromptEntityStore();
    }

    @Bean
    StateEntityServiceImpl<Prompt> _promptStateEntityService_(
            @Qualifier("promptEntityStm") STM<Prompt> stm,
            @Qualifier("promptActionsInfoProvider") STMActionsInfoProvider promptInfoProvider,
            @Qualifier("promptEntityStore") EntityStore<Prompt> entityStore) {
        return new StateEntityServiceImpl<>(stm, promptInfoProvider, entityStore);
    }

    @Bean
    GenericEntryAction<Prompt> promptEntryAction(@Qualifier("promptEntityStore") EntityStore<Prompt> entityStore,
            @Qualifier("promptActionsInfoProvider") STMActionsInfoProvider promptInfoProvider,
            @Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericEntryAction<Prompt> entryAction = new GenericEntryAction<Prompt>(entityStore, promptInfoProvider);
        stmFlowStore.setEntryAction(entryAction);
        return entryAction;
    }

    @Bean
    GenericExitAction<Prompt> promptExitAction(@Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore) {
        GenericExitAction<Prompt> exitAction = new GenericExitAction<Prompt>();
        stmFlowStore.setExitAction(exitAction);
        return exitAction;
    }

    @Bean
    XmlFlowReader promptFlowReader(@Qualifier("promptFlowStore") STMFlowStoreImpl flowStore) throws Exception {
        XmlFlowReader flowReader = new XmlFlowReader(flowStore);
        flowReader.setFilename(FLOW_DEFINITION_FILE);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FLOW_DEFINITION_FILE)) {
            if (inputStream != null) {
                flowReader.parse(inputStream);
                System.err.println("Loaded Flows: " + flowStore.getAllFlows().size());
                System.err.println("Loaded States: " + flowStore.getAllStates().size());
            } else {
                throw new RuntimeException("Could not find flow file in classpath: " + FLOW_DEFINITION_FILE);
            }
        }
        return flowReader;
    }

    @Bean
    PromptHealthChecker promptHealthChecker() {
        return new PromptHealthChecker();
    }

    @Bean
    STMTransitionAction<Prompt> defaultpromptSTMTransitionAction() {
        return new DefaultSTMTransitionAction<MinimalPayload>();
    }

    @Bean
    STMTransitionActionResolver promptTransitionActionResolver(
            @Qualifier("defaultpromptSTMTransitionAction") STMTransitionAction<Prompt> defaultSTMTransitionAction) {
        return new STMTransitionActionResolver(PREFIX_FOR_RESOLVER, defaultSTMTransitionAction);
    }

    @Bean
    StmBodyTypeSelector promptBodyTypeSelector(
            @Qualifier("promptActionsInfoProvider") STMActionsInfoProvider promptInfoProvider,
            @Qualifier("promptTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver) {
        return new StmBodyTypeSelector(promptInfoProvider, stmTransitionActionResolver);
    }

    @Bean
    STMTransitionAction<Prompt> promptBaseTransitionAction(
            @Qualifier("promptTransitionActionResolver") STMTransitionActionResolver stmTransitionActionResolver,
            @Qualifier("promptActivityChecker") ActivityChecker activityChecker,
            @Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore) {
        BaseTransitionAction<Prompt> baseTransitionAction = new BaseTransitionAction<>(stmTransitionActionResolver);
        baseTransitionAction.activityChecker = activityChecker;
        stmFlowStore.setDefaultTransitionAction(baseTransitionAction);
        return baseTransitionAction;
    }

    @Bean
    ActivityChecker promptActivityChecker(@Qualifier("promptFlowStore") STMFlowStoreImpl stmFlowStore) {
        return new ActivityChecker(stmFlowStore);
    }

    @Bean
    AreActivitiesComplete activitiesCompletionCheck(
            @Qualifier("promptActivityChecker") ActivityChecker activityChecker) {
        return new AreActivitiesComplete(activityChecker);
    }

    // --- Action Bean Definitions ---

    @Bean
    SubmitPromptAction submitPromptAction() {
        return new SubmitPromptAction();
    }

    @Bean
    AddCommentAction addCommentAction() {
        return new AddCommentAction();
    }

    @Bean
    EditPromptAction editPromptAction() {
        return new EditPromptAction();
    }

    @Bean
    AddTagAction addTagAction() {
        return new AddTagAction();
    }

    @Bean
    RemoveTagAction removeTagAction() {
        return new RemoveTagAction();
    }

    @Bean
    AddRevisionAction addRevisionAction() {
        return new AddRevisionAction();
    }

    @Bean
    AddAnswerAction addAnswerAction() {
        return new AddAnswerAction();
    }

    @Bean
    AcceptAnswerAction acceptAnswerAction() {
        return new AcceptAnswerAction();
    }

    @Bean
    UpvoteAction upvoteAction() {
        return new UpvoteAction();
    }

    @Bean
    DownvoteAction downvoteAction() {
        return new DownvoteAction();
    }

    @Bean
    AddBountyAction addBountyAction() {
        return new AddBountyAction();
    }

    @Bean
    AwardBountyAction awardBountyAction() {
        return new AwardBountyAction();
    }

    @Bean
    FlagPromptAction flagPromptAction() {
        return new FlagPromptAction();
    }

    @Bean
    ClosePromptAction closePromptAction() {
        return new ClosePromptAction();
    }

    @Bean
    MarkDuplicateAction markDuplicateAction() {
        return new MarkDuplicateAction();
    }

    @Bean
    AddFavoriteAction addFavoriteAction() {
        return new AddFavoriteAction();
    }

    @Bean
    RemoveFavoriteAction removeFavoriteAction() {
        return new RemoveFavoriteAction();
    }

    @Bean
    RecordImpressionAction recordImpressionAction() {
        return new RecordImpressionAction();
    }

    @Bean
    ReopenPromptAction reopenPromptAction() {
        return new ReopenPromptAction();
    }

    @Bean
    RemoveVoteAction removeVoteAction() {
        return new RemoveVoteAction();
    }

    @Bean
    ApprovePromptAction approvePromptAction() {
        return new ApprovePromptAction();
    }

    @Bean
    RejectPromptAction rejectPromptAction() {
        return new RejectPromptAction();
    }

    @Bean
    ValidatePromptAction validatePromptAction() {
        return new ValidatePromptAction();
    }

    @Bean
    public DeprecatePromptAction deprecatePromptAction() {
        return new DeprecatePromptAction();
    }

    @Bean
    public EnablementStrategy promptEnablementStrategy(ConfigProvider promptConfigProvider) {
        return new ConfigBasedEnablementStrategy(promptConfigProvider, PREFIX_FOR_PROPERTIES);
    }

    @Bean
    public ConfigProvider promptConfigProvider() {
        return new ConfigProviderImpl();
    }

    @Bean
    SecurityService securityService() {
        return new PromptSecurityService();
    }

}
