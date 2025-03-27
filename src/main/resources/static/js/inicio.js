document.addEventListener("DOMContentLoaded", function () {
  const currentYear = new Date().getFullYear();
  const footerText = document.querySelector("footer p");
  footerText.innerHTML = `&copy; ${currentYear} La Casa de Electricidad. Todos los derechos reservados.`;

  // Resaltar la página actual en el menú
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll(".nav-buttons a");

  navLinks.forEach((link) => {
    const href = link.getAttribute("href");
    if (href === currentPath || currentPath.includes(href)) {
      link.style.backgroundColor = "#2980b9";
      link.style.fontWeight = "bold";
    }
  });
});
