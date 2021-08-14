package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.pipelines.CdkPipeline;
import software.amazon.awscdk.pipelines.SimpleSynthAction;
import software.amazon.awscdk.pipelines.StandardNpmSynthOptions;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.GitHubTrigger;

public class AwsCdkJavaPipelineStack extends Stack {
    public AwsCdkJavaPipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsCdkJavaPipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Artifact sourceArtifact = new Artifact();
        final Artifact cloudAssemblyArtifact = new Artifact();

        final CdkPipeline pipeline = CdkPipeline.Builder.create(this, "Pipeline")
                .pipelineName("awsCdkJavaPipeline")
                .cloudAssemblyArtifact(cloudAssemblyArtifact)
                .sourceAction(GitHubSourceAction.Builder.create()
                        .actionName("GitHub")
                        .output(sourceArtifact)
                        .oauthToken(SecretValue.secretsManager("GITHUB_TOKEN_NAME"))
                        .trigger(GitHubTrigger.POLL)
//                        .owner("GITHUB-OWNER")
//                        .repo("GITHUB-REPO")
                        .owner("yoga2005live")
                        .repo("awsCdkJava")
                        .build())
                .synthAction(SimpleSynthAction.standardNpmSynth(
                        StandardNpmSynthOptions.builder()
                                .sourceArtifact(sourceArtifact)
                                .cloudAssemblyArtifact(cloudAssemblyArtifact)
                                .buildCommand("npm run build")
                                .build()))
                .build();
    }
}
