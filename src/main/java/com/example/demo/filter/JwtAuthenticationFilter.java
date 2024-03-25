package com.example.demo.filter;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Les filtres sont des composants utilisés pour intercepter et traiter les requêtes HTTP entrantes et sortantes avant qu'elles n'atteignent les servlets ou après qu'elles les aient quittés.
// Les exemples courants d'utilisation de filtres incluent la gestion des sessions, la sécurité, la compression, le journal, etc.

// OncePerRequestFilter : 1) créer des filtres personnalisés pour le traitement des requêtes HTTP entrantes et sortantes dans une application Web
//                        2) Elle garantit que le filtre est exécuté uniquement une fois par requête, même si le cycle de vie de la requête implique plusieurs appels à la chaîne de filtres.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // définir la logique spécifique que vous souhaitez exécuter pour chaque requête
    // HTTP traitée par le filtre
    // permet d'intercepter et de modifier les requêtes et les réponses avant
    // qu'elles ne soient traitées par d'autres parties de votre application.
    @Override
    protected void doFilterInternal(

            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain

    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Cette variable contient la valeur de l'en-tête "Authorization" de la requête
        // HTTP, qui est censée contenir le token JWT.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // substring : extraire une sous-chaîne
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);// extraire userEmail dans JWT token

        // NOTICE THIS
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // check if the token is valid or not
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // UsernamePasswordAuthenticationToken utilisé pour représenter les informations
                // d'authentification d'un utilisateur, telles que le nom d'utilisateur (ou
                // l'identifiant), le mot de passe et les autorisations (rôles) associées à cet
                // utilisateur.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Ajout de détails supplémentaires (Adresse IP de l'utilisateur ,URL de la
                // requete, information de géolocalisation)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // update securité contexte holder
                // utilisé pour gérer et stocker les informations d'authentification et
                // d'autorisation associées à l'utilisateur actuellement connecté dans une
                // application Java.
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        // indiquer au filtre actuel de continuer le traitement de la requête en la
        // passant au filtre suivant dans la chaîne
        filterChain.doFilter(request, response);

    }

}