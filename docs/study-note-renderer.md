# Study Note Renderer

This feature renders the portable course documents maintained in the separate `computer-science-teacher-prep` repository.

## Format v1 targets

### Baseline Markdown
- headings H1–H6
- paragraphs and inline emphasis
- ordered/unordered/nested/task lists
- links and anchors
- images
- blockquotes
- horizontal rules
- inline/fenced code
- tables with narrow-screen horizontal scrolling
- footnotes

### Rich blocks
- LaTeX-style inline/display math
- Mermaid diagrams with zoom/pan and source fallback
- safe legacy HTML subset only
- Encyclopedia directives: `note`, `tip`, `warning`, `exam-trap`, `remember`, `exam`, `mcq`, `reveal`, `definition`

### Study behavior
- interactive MCQs keep answers hidden until requested
- reveal blocks are collapsible
- exam/topic metadata becomes native badges
- diagrams/images are zoomable
- TTS consumes semantic text rather than Markdown syntax
- malformed/unknown blocks render a visible source fallback and never crash a lesson

## Security

Notes are content, not executable application code. Arbitrary JavaScript, script tags, iframes, remote executable content, event-handler attributes, and unsafe URLs must never execute.

## Architecture direction

Parsing and rendering are separate layers:

1. **Parser**: source Markdown/directives → typed document model.
2. **Renderer**: typed blocks → Compose UI.
3. **Accessibility projection**: typed document → speakable text.
4. **Fallback**: unknown/malformed source → explicit unsupported block.

The parser must be testable without Compose. Rendering should be exhaustive over the typed model so adding a new directive requires an intentional UI decision.

## Delivery sequence

1. typed document/block model
2. front-matter and directive parser
3. baseline Markdown block parser
4. native Compose renderer for text/lists/code/tables/callouts
5. interactive MCQ/reveal/definition blocks
6. image/diagram rendering
7. math and Mermaid adapters
8. TTS/accessibility projection
9. import/sync path from course repository or local package
10. golden/parser tests and malformed-content tests
