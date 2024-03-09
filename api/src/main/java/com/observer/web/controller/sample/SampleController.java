package com.observer.web.controller.sample;

import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.service.iptime.IptimeHealthService;
import com.observer.client.router.service.iptime.IptimeUsersService;
import com.observer.client.router.support.dto.request.WifiHealthServiceRequest;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.client.router.support.dto.response.RouterHealthResponse;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.web.controller.dto.sample.SampleBrowseRequest;
import com.observer.web.controller.validator.sample.SampleBrowseRequestValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

	private final SampleBrowseRequestValidator sampleBrowseRequestValidator;

	private final IptimeHealthService iptimeHealthService;
	private final IptimeAuthService iptimeAuthService;
	private final IptimeUsersService iptimeUsersService;

	@GetMapping()
	public String beta(Model model) {
		model.addAttribute("request", new SampleBrowseRequest());
		return "sample.html";
	}

	@PostMapping()
	public String betaRequest(
			@ModelAttribute("request") SampleBrowseRequest request,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		sampleBrowseRequestValidator.validate(request, result);
		if (result.hasErrors()) {
			List<String> errors =
					result.getAllErrors().stream()
							.map(DefaultMessageSourceResolvable::getCode)
							.collect(Collectors.toList());
			redirectAttributes.addFlashAttribute("errors", errors);
			return "sample.html";
		} else {
			try {
				WifiHealthServiceRequest hostRequest =
						WifiHealthServiceRequest.builder()
								.host(request.getHost() + ":" + request.getPort())
								.build();
				RouterHealthResponse healthResponse = iptimeHealthService.execute(hostRequest);
				IptimeAuthServiceRequest authRequest =
						IptimeAuthServiceRequest.builder()
								.host(request.getHost() + ":" + request.getPort())
								.userName(request.getCertification())
								.password(request.getPassword())
								.build();
				RouterAuthResponse authResponse = iptimeAuthService.execute(authRequest);
				IptimeUsersServiceRequest browseRequest =
						IptimeUsersServiceRequest.builder()
								.authInfo(authResponse.getResponse().getAuth())
								.host(request.getHost() + ":" + request.getPort())
								.build();
				RouterUsersResponse usersResponse = iptimeUsersService.execute(browseRequest);
				redirectAttributes.addFlashAttribute("users", usersResponse.getResponse().getUsers());
				return "redirect:/sample";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("connectError", "죄송합니다. 조회에 실패했습니다.");
				return "redirect:/sample";
			}
		}
	}
}
