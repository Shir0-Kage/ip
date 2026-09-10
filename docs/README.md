# Benjamin User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Tagging tasks

Tags are short labels you attach to a task, so related tasks can be pulled up
together later. A task can carry any number of them.

### Adding a tag

Format: `tag TASK_NUMBER TAG`

The leading `#` is optional, and tags are not case sensitive, so `#Fun`, `fun`
and `#fun` all mean the same tag. A tag may contain letters, digits, hyphens and
underscores. Add one tag at a time.

Example: `tag 1 #fun`

```
Nice, I've tagged this task:
  [T][ ] read book #fun
```

Tagging a task that already has that tag changes nothing:

```
That task is already tagged #fun.
```

### Removing a tag

Format: `untag TASK_NUMBER TAG`

Example: `untag 1 #fun`

```
OK, I've removed that tag:
  [T][ ] read book
```

### Finding tasks by tag

`find` searches tags as well as descriptions, so either spelling works.

Example: `find #fun`

```
Here are the matching tasks in your list:
1.[T][ ] read book #fun
2.[D][ ] essay (by: Oct 15 2019) #fun
```

Tags are saved with your tasks, so they are still there the next time you start
Benjamin. Task lists saved before tagging existed continue to load normally.

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
