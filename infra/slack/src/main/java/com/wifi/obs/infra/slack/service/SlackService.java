package com.wifi.obs.infra.slack.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.TextObject;
import com.wifi.obs.infra.slack.config.SlackChannel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackService {

	@Value(value = "${slack.token}")
	private String token;

	public void sendSlackMessage(String message, SlackChannel channel) {
		MethodsClient methods = Slack.getInstance().methods(token);

		try {
			methods.chatPostMessage(
					req ->
							req.channel(channel.getChannelName())
									.blocks(
											Blocks.asBlocks(
													getMessageSectionBlock(message),
													getDefaultAndMessageTypeSectionBlock(channel))));
		} catch (SlackApiException | IOException e) {
			log.error(e.getMessage());
		}

		log.info("Slack message sent. Channel: {}, Message: {}", channel.getChannelName(), message);
	}

	public static SectionBlock getMessageSectionBlock(String message) {
		return SectionBlock.builder().text(MarkdownTextObject.builder().text(message).build()).build();
	}

	public static SectionBlock getDefaultAndMessageTypeSectionBlock(SlackChannel channel) {
		return SectionBlock.builder()
				.fields(List.of(typeTextObject(channel), whenTexObject(), reasonTextObject(channel)))
				.build();
	}

	public static TextObject typeTextObject(SlackChannel channel) {
		return MarkdownTextObject.builder().text("*Type:*\n" + channel.getType()).build();
	}

	public static TextObject whenTexObject() {
		return MarkdownTextObject.builder().text("*When:*\n" + LocalDateTime.now()).build();
	}

	public static TextObject reasonTextObject(SlackChannel channel) {
		return MarkdownTextObject.builder().text("*Reason:*\n" + channel.getReason()).build();
	}
}
