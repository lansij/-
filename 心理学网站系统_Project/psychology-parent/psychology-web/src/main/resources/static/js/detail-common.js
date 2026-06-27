// detail-common.js — shared comment & favorite logic for detail pages
// Requires: global var `currentId` and `targetType` set before loading this script

function escapeHtml(str) {
    var div = document.createElement('div');
    div.appendChild(document.createTextNode(str));
    return div.innerHTML;
}

function renderContent(text) {
    if (!text) return '<p>暂无内容</p>';
    // If content already contains HTML tags, render as-is
    if (/<[a-z][\s\S]*>/i.test(text)) return text;
    // Plain text: wrap each line in <p>
    var lines = text.split(/\n+/).filter(function(l) { return l.trim(); });
    if (lines.length === 0) return '<p>暂无内容</p>';
    return lines.map(function(l) { return '<p>' + escapeHtml(l) + '</p>'; }).join('');
}

function loadComments() {
    layui.$.post('/comment/getByTarget', { targetType: targetType, targetId: currentId }, function(list) {
        var container = document.getElementById('commentList');
        if (!list || list.length === 0) {
            container.innerHTML = '<div class="no-comment">暂无评论，快来抢沙发吧~</div>';
            return;
        }
        var html = '';
        list.forEach(function(c) {
            var time = c.createTime ? c.createTime.substring(0, 16).replace('T', ' ') : '';
            html += '<div class="comment-item"><div class="comment-header"><span class="comment-user">用户 #' + c.userId + '</span><span class="comment-time">' + time + '</span></div><div class="comment-body">' + escapeHtml(c.content || '') + '</div></div>';
        });
        container.innerHTML = html;
    });
}

function submitComment() {
    var textarea = document.getElementById('commentContent');
    var content = textarea.value.trim();
    if (!content) { layui.layer.msg('请输入评论内容'); return; }
    layui.$.post('/comment/add', { targetType: targetType, targetId: currentId, content: content }, function(res) {
        if (res.code == 0) {
            textarea.value = '';
            layui.layer.msg('评论成功', {icon: 1});
            loadComments();
        } else {
            layui.layer.msg(res.msg || '评论失败', {icon: 2});
        }
    });
}

function toggleFavorite() {
    var btn = document.getElementById('favBtn');
    var isActive = btn.classList.contains('active');
    var url = isActive ? '/favorite/remove' : '/favorite/add';
    layui.$.post(url, { targetType: targetType, targetId: currentId }, function(res) {
        if (res.code == 0) {
            if (isActive) {
                btn.classList.remove('active');
                btn.textContent = '☆ 收藏';
            } else {
                btn.classList.add('active');
                btn.textContent = '★ 已收藏';
            }
        } else {
            layui.layer.msg(res.msg || '操作失败', {icon: 2});
        }
    });
}

function checkFavorite() {
    var btn = document.getElementById('favBtn');
    layui.$.post('/favorite/check', { targetType: targetType, targetId: currentId }, function(res) {
        if (res) {
            btn.classList.add('active');
            btn.textContent = '★ 已收藏';
        }
    });
}
