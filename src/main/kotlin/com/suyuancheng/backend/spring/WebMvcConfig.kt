package com.suyuancheng.backend.spring

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import org.springframework.web.servlet.resource.ResourceResolverChain
import javax.servlet.http.HttpServletRequest

/**
 * @author hsj
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/front/")
            .resourceChain(true)
            .addResolver(FallbackPathResourceResolver())
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login").setViewName("index.html")
    }

    class FallbackPathResourceResolver : PathResourceResolver() {
        override fun resolveResource(
            request: HttpServletRequest?,
            requestPath: String,
            locations: MutableList<out Resource>,
            chain: ResourceResolverChain
        ): Resource? {
            return super.resolveResource(request, requestPath, locations, chain)
                ?: super.resolveResource(request, "/index.html", locations, chain)
        }
    }

}