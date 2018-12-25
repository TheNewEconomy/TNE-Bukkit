<div class="page-content">
    <p class="description">
        <label class="indent">Welcome</label> to your interactive economy experience. From this control panel you may do
        things such as check your balance, pay other players, or check on your shops. If you're an administrator, you may
        do things such as remotely delete shops, give/take to/from players, and check statistics.
    </p>
    <h2>Top Balances</h2>
    <div class="topBalances">
        <#list topBalances as topBal>
            <div class="topEntry">
                <img class="topHead" src="https://crafatar.com/avatars/${topBal.uuid}?overlay" height="40px" width="40px" />
                <label class="topUsername">${topBal.username}</label>
                <label class="topAmount">${topBal.amount}</label>
            </div>
        </#list>
    </div>

</div>
<#include "../container.ftl">
<#include "../footer.ftl">