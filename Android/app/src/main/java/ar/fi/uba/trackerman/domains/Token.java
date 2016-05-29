package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by smpiano on 28/5/16.
 */
public class Token {
    private String token;
    private Seller seller;

    public Token(String token) {
        this.token = token;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getToken() {
        return token;
    }

    public static Token fromJson(JSONObject json) {
        try {
            Token token = new Token(json.getString("token"));
            token.setSeller(Seller.fromJson(json.getJSONObject("seller")));
            return token;
        } catch (JSONException e) {
            throw new BusinessException("Error parsing Token.", e);
        }
    }
}
