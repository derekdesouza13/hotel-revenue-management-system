function Header() {
  return (
    <header className="header">
      <div>
        <h1>Hotel Revenue Management</h1>
        <p>Revenue operations and performance overview</p>
      </div>

      <div className="header-status">
        <span className="status-dot"></span>
        <span>System Online</span>
      </div>
    </header>
  );
}

export default Header;