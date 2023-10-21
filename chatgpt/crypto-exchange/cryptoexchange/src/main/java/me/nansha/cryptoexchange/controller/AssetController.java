package me.nansha.cryptoexchange.controller;

import me.nansha.cryptoexchange.model.Asset;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<Asset> listAllAssets() {
        var a = new Asset("Bitcoin");
        var b = new Asset("Ethereum");
        return List.of(a, b);
        // Return a list of all assets
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public Asset getAssetById(@PathVariable Long id) {
        // Return details of the specific asset by ID
        var a = new Asset("Bitcoin");
        return a;
    }
}