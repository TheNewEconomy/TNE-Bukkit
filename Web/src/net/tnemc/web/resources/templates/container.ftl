<header>
    <div class="user-bar">
        <div class="head">
            <img src="https://crafatar.com/avatars/${user.uuid}?overlay" height="40px" width="40px" />
            <label class="username">${user.display}</label>
        </div>
        <div class="balances">
            <div class="entry">
                <label class="currency">Gold:</label>
                <label class="amount">${user.gold}</label>
            </div>
        </div>
    </div>
    <nav>
        <ul>
            <li><a href="/overview">Overview</a></li>
            <li><a href="/account">Account</a></li>
            <li><a href="/shops">Shops</a></li>
            <li><a href="/shops">Admin</a></li>
        </ul>
    </nav>
    <div class="header-space"></div>
</header>
<div class="main-content">
    <h1>${header}</h1>
</div>