package com.wifi.obs.app.web.controller.beta;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.service.wifi.iptime.GetIptimeUsersService;
import com.wifi.obs.app.web.controller.validator.beta.IptimeBetaRequestValidator;
import com.wifi.obs.app.web.dto.request.beta.IptimeBetaRequest;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/beta")
@RequiredArgsConstructor
public class BetaController {

	private final GetIptimeUsersService getIptimeUsersService;
	private final IptimeBetaRequestValidator iptimeBetaRequestValidator;

	@GetMapping("/iptime")
	public String beta(Model model) {
		model.addAttribute("request", new IptimeBetaRequest());
		return "IptimeBeta.html";
	}

	@PostMapping("/iptime")
	public String betaRequest(
			@ModelAttribute("request") IptimeBetaRequest request,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			HttpServletResponse response) {
		iptimeBetaRequestValidator.validate(request, result);
		if (result.hasErrors()) {
			List<String> errors =
					result.getAllErrors().stream()
							.map(DefaultMessageSourceResolvable::getCode)
							.collect(Collectors.toList());
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/beta/iptime";
		} else {
			try {
				OnConnectUserInfos res = getIptimeUsersService.execute(request);
				redirectAttributes.addFlashAttribute("userInfos", res);
				return "redirect:/beta/iptime/response";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("connectError", "죄송합니다. 조회에 실패했습니다.");
				return "redirect:/beta/iptime";
			}
		}
	}

	@GetMapping("/iptime/response")
	public String betaResponse(@ModelAttribute("userInfos") OnConnectUserInfos infos, Model model) {
		model.addAttribute("userInfos", infos.getUserInfos());
		return "IptimeBetaResponse.html";
	}
}
