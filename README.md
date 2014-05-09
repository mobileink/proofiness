# proofiness

Proofing, testing, documentation for Clojure

## Colbert Logic

Instead of truth, truthiness; instead of true and false, truthy and falsey.

Only one logical operator, "andy".  Defined by the following truthiness table:

| a | b | a andy b |
|---|---|----------|
| truthy | truthy | truthy |
| truthy | falsey | truthy |
| falsey | truthy | truthy |
| falsey | falsey | falsey |
