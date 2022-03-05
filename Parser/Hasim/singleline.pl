#!/usr/bin/perl -w
use strict;

# Take the output of lookup and convert it into one line per word
# format with multiple parses on the same line separated by spaces

my $word;
my @parses;
while(<>) {
    if (/^\s*$/) {
	next if not defined $word;
	print join(" ", $word, @parses) . "\n";
	undef $word;
	undef @parses;
    } else {
	my ($w, $s, $p) = split(/[\t\r\n]/);
	die "Bad line [$_]" if not defined $p;
	$word = $w if not defined $word;
	die "Bad word [$_]" if $w ne $word;
	push @parses, $s.$p;
    }
}
