/**
 * Author   : Prabakaran Ramu
 * User     : ramup
 * Date     : 02/07/2025
 * Usage    :
 * Since    : Version 1.0
 */
package org.me.practise.personaggregator.entity;

import java.util.Objects;

public class SoapAccount {
    private String username;

    public SoapAccount (String username) {
        this.username = username;
    }

    public String username () {
        return username;
    }

    public SoapAccount setUsername (String username) {
        this.username = username;
        return this;
    }

    @Override
    public final boolean equals (Object o) {
        if ( !(o instanceof SoapAccount that) ) return false;

        return Objects.equals (username, that.username);
    }

    @Override
    public int hashCode () {
        return Objects.hashCode (username);
    }
}
