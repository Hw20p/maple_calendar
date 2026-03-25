// 폼 토글 (캘린더 day.html에서 사용)
function toggleForm(id) {
    const el = document.getElementById(id);
    if (el) el.classList.toggle('hidden');
}

// 다크/라이트 모드 토글
function toggleTheme() {
    const isLight = document.documentElement.classList.toggle('light-mode');
    localStorage.setItem('theme', isLight ? 'light' : 'dark');
    document.getElementById('themeToggle').textContent = isLight ? '🌙' : '☀️';
}

// 알림 메시지 자동 사라짐
document.addEventListener('DOMContentLoaded', () => {
    // 버튼 아이콘 동기화 (클래스는 이미 <head>에서 적용됨)
    if (document.documentElement.classList.contains('light-mode')) {
        const btn = document.getElementById('themeToggle');
        if (btn) btn.textContent = '🌙';
    }

    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 3000);
    });
});
