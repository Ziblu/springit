package com.ziblu.springit.controller;

import com.ziblu.springit.domain.Comment;
import com.ziblu.springit.domain.Link;
import com.ziblu.springit.domain.User;
import com.ziblu.springit.service.CommentService;
import com.ziblu.springit.service.LinkService;
import com.ziblu.springit.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private LinkService linkService;

    private CommentService commentService;

    private UserService userService;

    public LinkController(LinkService linkService, CommentService commentService, UserService userService) {
        this.linkService = linkService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String list(Model model){
        model.addAttribute("links", linkService.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id,Model model) {
        Optional<Link> link = linkService.findById(id);
        if( link.isPresent() ) {
            Link currentLink = link.get();
            Comment comment = new Comment();
            comment.setLink(currentLink);
            model.addAttribute("comment",comment);
            model.addAttribute("link",currentLink);
            model.addAttribute("success", model.containsAttribute("success"));
            return "link/view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors were found while submitting a new link.");
            model.addAttribute("link", link);
            return "link/submit";
        } else {
            // Cast principal to Authentication and get User
            Authentication authentication = (Authentication) principal;
            String email = authentication.getName();
            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                link.setUser(user);

                // Save the link
                linkService.save(link);
                logger.info("New Link was saved successfully.");
                redirectAttributes.addAttribute("id", link.getId()).addFlashAttribute("success", true);
                return "redirect:/link/{id}";
            } else {
                logger.error("User not found for email: " + email);
                // Handle user not found case (redirect or error page)
                return "redirect:/login";
            }
        }
    }


    @Secured({"ROLE_USER"})
    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if( bindingResult.hasErrors() ) {
            logger.info("Something went wrong.");
        } else {
            logger.info("New Comment Saved!");
            commentService.save(comment);
        }
        return "redirect:/link/" + comment.getLink().getId();
    }
}