package com.babyblackdog.ddogdog.wishlist.service;

import static com.babyblackdog.ddogdog.global.exception.ErrorCode.INVALID_WISHLIST_PERMISSION;

import com.babyblackdog.ddogdog.global.exception.WishlistException;
import com.babyblackdog.ddogdog.wishlist.model.Email;
import com.babyblackdog.ddogdog.wishlist.model.Wishlist;
import com.babyblackdog.ddogdog.wishlist.service.dto.WishlistResult;
import com.babyblackdog.ddogdog.wishlist.service.dto.WishlistResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistReader reader;
    private final WishlistStore store;

    public WishlistServiceImpl(WishlistReader reader, WishlistStore store) {
        this.reader = reader;
        this.store = store;
    }

    @Transactional
    @Override
    public WishlistResult registerWishlist(String email, Long placeId) {
        Wishlist savedWishlist = store.registerWishlist(new Wishlist(new Email(email), placeId));
        return WishlistResult.of(savedWishlist);
    }

    @Transactional
    @Override
    public void deleteWishlist(Long wishlistId) {
        JwtSimpleAuthentication jwt = JwtSimpleAuthentication.getInstance();
        String email = jwt.getEmail();

        Wishlist wishlist = reader.findWishlistById(wishlistId);
        if (!email.equals(wishlist.getEmail())) {
            throw new WishlistException(INVALID_WISHLIST_PERMISSION);
        }

        store.deleteWishlist(wishlistId);
    }

    @Override
    public WishlistResults findWishlistsByEmail(String email, Pageable pageable) {
        Page<Wishlist> retrievedWishlists = reader.findWishlistsByEmail(email, pageable);
        return WishlistResults.of(retrievedWishlists);
    }
}