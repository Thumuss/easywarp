# Building the Documentation

This documentation is built using [mdBook](https://rust-lang.github.io/mdBook/), a utility to create modern online books from Markdown files.

## Prerequisites

### Install mdBook

#### Using Cargo (Rust)

```bash
cargo install mdbook
```

#### Using Pre-built Binaries

1. Go to [mdBook releases](https://github.com/rust-lang/mdBook/releases)
2. Download the appropriate binary for your system
3. Extract and add to your PATH

#### Using Package Managers

**macOS (Homebrew)**:
```bash
brew install mdbook
```

**Linux (Cargo)**:
```bash
cargo install mdbook
```

**Windows (Scoop)**:
```bash
scoop install mdbook
```

## Building the Documentation

### Navigate to docs folder

```bash
cd docs/
```

### Build the documentation

```bash
mdbook build
```

This generates the HTML documentation in `docs/book/`.

### Serve locally (with live reload)

```bash
mdbook serve
```

Then open your browser at: `http://localhost:3000`

Changes to markdown files will automatically refresh the browser!

### Open the built documentation

After building, open `docs/book/index.html` in your web browser.

## Documentation Structure

```
docs/
├── book.toml              # mdBook configuration
├── custom.css             # Custom styling
├── src/
│   ├── SUMMARY.md         # Table of contents
│   ├── introduction.md    # Homepage
│   ├── getting-started/   # Installation & quick start
│   ├── configuration/     # Configuration guides
│   ├── usage/             # Usage documentation
│   ├── developers/        # API & events
│   ├── examples/          # Examples
│   └── support/           # FAQ & troubleshooting
└── book/                  # Generated HTML (after build)
```

## Editing Documentation

### Add a new page

1. Create a new `.md` file in the appropriate directory under `src/`
2. Add it to `src/SUMMARY.md`:

```markdown
# Summary

[Introduction](./introduction.md)

# My Section

- [New Page](./mysection/newpage.md)
```

3. Build and view: `mdbook serve`

### Markdown Features

mdBook supports:
- Standard Markdown
- Code blocks with syntax highlighting
- Tables
- Internal links: `[Link Text](./other-page.md)`
- Anchor links: `[Link](#section)`
- Images: `![Alt text](./image.png)`
- Admonitions (via custom CSS)

### Code Blocks

````markdown
```java
public class Example {
    public static void main(String[] args) {
        System.out.println("Hello!");
    }
}
```
````

### Tables

```markdown
| Column 1 | Column 2 |
|----------|----------|
| Value 1  | Value 2  |
```

## Deployment

### Option 1: GitHub Pages

1. Build the documentation:
   ```bash
   mdbook build
   ```

2. Push the `book/` directory to a `gh-pages` branch:
   ```bash
   git subtree push --prefix docs/book origin gh-pages
   ```

3. Enable GitHub Pages in repository settings

### Option 2: Manual Hosting

1. Build the documentation
2. Upload the entire `book/` directory to your web server
3. Point your web server to serve `book/index.html`

### Option 3: Netlify/Vercel

1. Connect your repository
2. Set build command: `cd docs && mdbook build`
3. Set publish directory: `docs/book`

## Customization

### Modify Styling

Edit `custom.css` to customize appearance:

```css
/* Example: Change primary color */
:root {
    --color-primary: #FF5722;
}

/* Example: Custom table styling */
table th {
    background-color: var(--color-primary);
}
```

### Configure mdBook

Edit `book.toml`:

```toml
[book]
title = "My Documentation"
authors = ["Your Name"]

[output.html]
default-theme = "navy"
git-repository-url = "https://github.com/user/repo"
```

## Useful Commands

```bash
# Build documentation
mdbook build

# Serve with live reload
mdbook serve

# Serve on specific port
mdbook serve --port 8080

# Open in browser automatically
mdbook serve --open

# Build and watch for changes
mdbook watch

# Clean build directory
mdbook clean

# Test all links
mdbook test

# Check for errors without building
mdbook check
```

## Troubleshooting

### mdBook not found

Ensure mdBook is in your PATH:

```bash
which mdbook  # Unix/Mac
where mdbook  # Windows
```

### Port already in use

Use a different port:

```bash
mdbook serve --port 3001
```

### Changes not reflecting

1. Stop the server (Ctrl+C)
2. Clean build: `mdbook clean`
3. Rebuild: `mdbook build`
4. Serve again: `mdbook serve`

### CSS not loading

Check that `custom.css` is in the correct location:
- Should be: `docs/custom.css`
- Referenced in `book.toml`

## Contributing

To contribute to the documentation:

1. Fork the repository
2. Make your changes in the `docs/src/` directory
3. Test locally with `mdbook serve`
4. Submit a pull request

## Resources

- [mdBook Documentation](https://rust-lang.github.io/mdBook/)
- [Markdown Guide](https://www.markdownguide.org/)
- [mdBook GitHub](https://github.com/rust-lang/mdBook)

## License

This documentation is part of the EasyWarp project and follows the same license.
