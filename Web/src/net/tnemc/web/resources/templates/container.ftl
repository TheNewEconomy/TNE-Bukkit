<#include "header.ftl">
<header>
    <div class="user-bar">
        <div class="head">
            <img src="https://crafatar.com/avatars/${user.uuid}?overlay" height="40px" width="40px" />
            <label class="username">${user.display}</label>
        </div>
        <div class="balances">
            <div class="entry">
                <#list balances as balance>
                    <label class="currency">${balance.currency}:</label>
                    <label class="amount">${balance.amount}</label>
                </#list>
            </div>
        </div>
    </div>
    <nav>
        <ul>
            <#list links as link>
                <li${link.active}><a href="${link.path}">${link.display}</a></li>
            </#list>
        </ul>
    </nav>
    <div class="header-space"></div>
</header>
<div class="main-content">
    <h1>${header}</h1>
</div>